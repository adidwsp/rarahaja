package com.example.poskedai;


import static android.app.Activity.RESULT_OK;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class CreateFragment extends Fragment {
    private static final int PICK_IMAGE_REQUEST = 1;
    private Uri imageUri;

    private ImageView menuImage;
    private Button selectImageButton;

    Spinner add_menu_type;
    FloatingActionButton add;
    EditText add_menu_name, add_menu_price, add_menu_remarks;
    Button btn_add_menu;
    ImageButton btn_back;
    DatabaseReference database = FirebaseDatabase.getInstance().getReference();
    private StorageReference storageReference = FirebaseStorage.getInstance().getReference("menu_images");

    @SuppressLint({"MissingInflatedId", "WrongViewCast", "CutPasteId"})
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_create, container, false);

        add_menu_type = view.findViewById(R.id.spinner_add_menu_type);
        add_menu_name = view.findViewById(R.id.add_menu_name);
        add_menu_price = view.findViewById(R.id.add_menu_price);
        add_menu_remarks = view.findViewById(R.id.add_menu_remarks);
        btn_add_menu = view.findViewById(R.id.btn_add_menu);
        btn_back = view.findViewById(R.id.btn_back);
        menuImage = view.findViewById(R.id.menu_image);
        selectImageButton = view.findViewById(R.id.btn_select_image);

        // Data untuk Spinner
        String[] items = new String[]{"Pondasi", "Koncian", "Camilan"};

        // Membuat adapter untuk Spinner
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_dropdown_item, items);
        add_menu_type.setAdapter(adapter);

        // Listener untuk Spinner
        add_menu_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing
            }
        });

        selectImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFileChooser();
            }

            private void openFileChooser() {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, PICK_IMAGE_REQUEST);
            }
        });

        btn_add_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String getMenu_type = add_menu_type.getSelectedItem().toString();
                String getMenu_name = add_menu_name.getText().toString();
                String getPrice = add_menu_price.getText().toString();
                String getMenu_remarks = add_menu_remarks.getText().toString();

                if (getMenu_name.isEmpty()) {
                    add_menu_name.setError("Nama Menu Tidak Boleh Kosong");
                } else if (getPrice.isEmpty()) {
                    add_menu_price.setError("Harga Menu Tidak Boleh Kosong");
                } else {
                    uploadImageAndSaveData(getMenu_type, getMenu_name, getPrice, getMenu_remarks);
                }
            }
        });

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                transaction.replace(R.id.content, new MenuFragment());
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

        return view;
    }

    private void uploadImageAndSaveData(String menuType, String menuName, String price, String menuRemarks) {
        if (imageUri != null) {
            StorageReference fileReference = storageReference.child(menuName + "." + getFileExtension(imageUri));

            fileReference.putFile(imageUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            fileReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    String imageUrl = uri.toString();
                                    saveDataToDatabase(menuType, menuName, price, menuRemarks, imageUrl);
                                }
                            });
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getActivity(), "Upload Gambar Gagal: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        } else {
            saveDataToDatabase(menuType, menuName, price, menuRemarks, null);
        }
    }

    private void saveDataToDatabase(String menuType, String menuName, String price, String menuRemarks, String imageUrl) {
        database.child("tb_menu").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                long count = snapshot.getChildrenCount() + 1;
                String id_menu = String.format("%04d", count);
                ModelDatabase newMenu = new ModelDatabase(id_menu, menuType, menuName, price, menuRemarks, imageUrl);
                database.child("tb_menu").child(id_menu).setValue(newMenu)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(getActivity(), "Data Berhasil Ditambahkan", Toast.LENGTH_SHORT).show();
                                loadFragment(new MenuFragment());
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(getActivity(), "Data Gagal Ditambahkan", Toast.LENGTH_SHORT).show();
                            }
                        });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getActivity(), "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private String getFileExtension(Uri uri) {
        ContentResolver contentResolver = getActivity().getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            menuImage.setImageURI(imageUri);
        }
    }

    private void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
        transaction.replace(R.id.content, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
