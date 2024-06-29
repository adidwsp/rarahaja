package com.example.poskedai;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class EditMenuFragment extends Fragment {
    private Uri imageUri;
    private ModelDatabase data;

    private EditText editMenuName, editMenuPrice, editMenuRemarks;
    private Spinner editMenuType;
    private static final int PICK_IMAGE_REQUEST = 1;
    Button selectImageButton, btn_update_menu, btn_delete_menu;
    ImageButton btn_back;
    DatabaseReference database = FirebaseDatabase.getInstance().getReference();
    private StorageReference storageReference = FirebaseStorage.getInstance().getReference("menu_images");
    private String id_menu;

    public EditMenuFragment() {
        // Required empty public constructor
    }

    public static EditMenuFragment newInstance(ModelDatabase data) {
        EditMenuFragment fragment = new EditMenuFragment();
        Bundle args = new Bundle();
        args.putSerializable("data", data);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            data = (ModelDatabase) getArguments().getSerializable("data");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_menu, container, false);

        editMenuType = view.findViewById(R.id.spinner_edit_menu_type);
        editMenuName = view.findViewById(R.id.edit_menu_name);
        editMenuPrice = view.findViewById(R.id.edit_menu_price);
        editMenuRemarks = view.findViewById(R.id.edit_menu_remarks);
        selectImageButton = view.findViewById(R.id.btn_select_image);
        btn_update_menu = view.findViewById(R.id.btn_update_menu);
        btn_delete_menu = view.findViewById(R.id.btn_delete_menu);
        btn_back = view.findViewById(R.id.btn_back);

        // Setting up the spinner
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),
                R.array.menu_categoty, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        editMenuType.setAdapter(adapter);

        // Set data to the views
        if (data != null) {
            editMenuName.setText(data.getMenu_name());
            editMenuPrice.setText(String.valueOf(data.getMenu_price()));
            editMenuRemarks.setText(data.getMenu_remarks());
            // Set the spinner selection based on data
            if (data.getMenu_type() != null) {
                int spinnerPosition = adapter.getPosition(data.getMenu_type());
                editMenuType.setSelection(spinnerPosition);
            }
        }


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

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getParentFragmentManager().popBackStack();
            }
        });

        btn_update_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String getMenu_type = editMenuType.getSelectedItem().toString();
                String getMenu_name = editMenuName.getText().toString();
                String getPrice = editMenuPrice.getText().toString();
                String getMenu_remarks = editMenuRemarks.getText().toString();

                if (getMenu_name.isEmpty()) {
                    editMenuName.setError("Nama Menu Tidak Boleh Kosong");
                } else if (getPrice.isEmpty()) {
                    editMenuPrice.setError("Harga Menu Tidak Boleh Kosong");
                } else {
                    if (imageUri != null) {
                        uploadImageAndSaveData(getMenu_type, getMenu_name, getPrice, getMenu_remarks);
                    } else {
                        // Jika tidak ada gambar baru yang diunggah, gunakan URL gambar lama jika tersedia
                        String imageUrl = data != null ? data.getImageUrl() : "";
                        saveDataToDatabase(data != null ? data.getId_menu() : null, getMenu_type, getMenu_name, getPrice, getMenu_remarks, imageUrl);
                    }
                }
            }
        });

        btn_delete_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (data != null) {
                    deleteMenuFromDatabase(data.getId_menu());
                }
            }
        });

        return view;
    }

    private void deleteMenuFromDatabase(String id_menu) {
        if (id_menu != null && !id_menu.isEmpty()) {
            database.child("tb_menu").child(id_menu).removeValue()
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Toast.makeText(getActivity(), "Menu berhasil dihapus", Toast.LENGTH_SHORT).show();
                            getParentFragmentManager().popBackStack();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getActivity(), "Gagal menghapus menu: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        } else {
            Toast.makeText(getActivity(), "ID Menu tidak valid", Toast.LENGTH_SHORT).show();
        }
    }

    private void uploadImageAndSaveData(String menuType, String menuName, String price, String menuRemarks) {
        StorageReference fileReference = storageReference.child(System.currentTimeMillis() + "." + getFileExtension(imageUri));

        fileReference.putFile(imageUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        fileReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                String imageUrl = uri.toString();
                                saveDataToDatabase(data != null ? data.getId_menu() : null, menuType, menuName, price, menuRemarks, imageUrl);
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
    }

    private String getFileExtension(Uri uri) {
        ContentResolver contentResolver = getActivity().getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    private void saveDataToDatabase(String menuId, String menuType, String menuName, String price, String menuRemarks, String imageUrl) {
        if (menuId != null && !menuId.isEmpty()) {
            // Update existing menu
            ModelDatabase updatedMenu = new ModelDatabase(menuId, menuType, menuName, price, menuRemarks, imageUrl);
            database.child("tb_menu").child(menuId).setValue(updatedMenu)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {

                        @Override
                        public void onSuccess(Void unused) {
                            Toast.makeText(getActivity(), "Data Berhasil Diperbarui", Toast.LENGTH_SHORT).show();
                            getParentFragmentManager().popBackStack();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getActivity(), "Data Gagal Diperbarui", Toast.LENGTH_SHORT).show();
                        }
                    });
        } else {
            // Add new menu
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
                                    getParentFragmentManager().popBackStack();
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



    }

}
