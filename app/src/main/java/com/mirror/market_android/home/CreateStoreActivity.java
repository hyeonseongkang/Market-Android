package com.mirror.market_android.home;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.mirror.market_android.R;

import java.io.ByteArrayOutputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class CreateStoreActivity extends AppCompatActivity {


    private static final String TAG = "CreateContent";

    boolean firstCheck = true;
    Uri firstUri;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("store");

    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageRef = storage.getReference();

    List<StoreData> list = new ArrayList<>();

    private RecyclerView createContentRecyclerView;
    private CreateContentActivityAdapter adapter;
    private LinearLayoutManager layoutManager;

    private List<PhotoData> photoDataList;

    private ImageView cancelButton;
    private RelativeLayout createButton, gallery;

    private DecimalFormat decimalFormat = new DecimalFormat("#,###");
    private EditText title, content, price;
    private TextView photoCount;

    private ProgressBar progress;


    private Uri photo;

    String user = "rgt9697";

    private String priceFormat, totalPriceFormat;

    ImageView tempPhoto;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_store);
        photoDataList = new ArrayList();


        cancelButton = (ImageView) findViewById(R.id.cancelButton);
        cancelButton.setEnabled(true);
        cancelButton.setClickable(true);

        gallery = (RelativeLayout) findViewById(R.id.gallery);
        gallery.setEnabled(true);
        gallery.setClickable(true);

        createButton = (RelativeLayout) findViewById(R.id.createButton);
        createButton.setEnabled(true);
        createButton.setClickable(true);

        photoCount = (TextView) findViewById(R.id.photoCount);

        title = (EditText) findViewById(R.id.title);
        content = (EditText) findViewById(R.id.content);
        price = (EditText) findViewById(R.id.price);

        progress = (ProgressBar) findViewById(R.id.progress);

        price.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!TextUtils.isEmpty(s.toString()) && !s.toString().equals(priceFormat)) {
                    priceFormat = decimalFormat.format(Double.parseDouble(s.toString().replaceAll(",", "")));
                    price.setText(priceFormat);
                    price.setSelection(priceFormat.length());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });



        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                intent.setType("image/*");
                launcher.launch(intent);
            }
        });

        tempPhoto = (ImageView) findViewById(R.id.tempPhoto);

        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String ti = title.getText().toString();
                String co = content.getText().toString();
                String priceString = price.getText().toString();

//                if (TextUtils.isEmpty(ti) || TextUtils.isEmpty(co)) {
//                    Log.d(TAG, "입력 사항을 확인해 주세요.");
//                    return;

                if (photoDataList.size() == 0) {
                    Toast.makeText(CreateStoreActivity.this, "이미지를 하나 이상 추가해 주세요.", Toast.LENGTH_SHORT).show();
                    return;
                }
                progress.setVisibility(View.VISIBLE);


                Log.d(TAG, "글 작성 내용");
                Log.d(TAG, "제목: " + ti);
                Log.d(TAG, "상세내용: " + co);

                String key = myRef.push().getKey();



                ArrayList<String> photoKeys = new ArrayList<>();
                ArrayList<Uri> photoUri = new ArrayList<>();

                for (int i = 0; i < photoDataList.size(); i++) {
                    String photoKey = myRef.push().getKey();
                    photoKeys.add(photoKey);
                    StorageReference tempStorage = storageRef.child("store/" + photoKey + ".jpg");
                    UploadTask uploadTask = tempStorage.putFile(photoDataList.get(i).getPhotoUri());

                    uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            if (firstCheck) {
                                tempStorage.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        photoUri.add(uri);
                                        System.out.println("이것이다!!: " + uri);
                                        firstUri = uri;
                                        StoreData create = new StoreData(user, ti, priceString, co, photoKeys, key, firstUri.toString());
                                        myRef.child(key).setValue(create);
                                        progress.setVisibility(View.GONE);
                                        finish();
                                        Toast.makeText(CreateStoreActivity.this, "글 작성 완료", Toast.LENGTH_LONG).show();
                                    }
                                });
                                firstCheck = false;
                            }

                            System.out.println("사진 저장 성공!!! " + taskSnapshot.getUploadSessionUri());
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure( Exception e) {
                            System.out.println("사진 저장 실패");
                        }
                    });
                }

                System.out.println("PhotoUri");
                for (int i = 0; i < photoUri.size(); i++) {
                    System.out.println(photoUri.get(i).toString());
                }

                System.out.println("PhotoKeys");
                for (int i = 0; i < photoKeys.size(); i++) {
                    System.out.println(photoKeys.get(i));
                }
                
            }
        });


        createContentRecyclerView = (RecyclerView) findViewById(R.id.createContentRecyclerView);
        createContentRecyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        createContentRecyclerView.setLayoutManager(layoutManager);

        adapter = new CreateContentActivityAdapter(photoDataList, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Object object = v.getTag();
                if (object != null) {
                    int removePosition = (int) object;
                    Log.d(TAG, String.valueOf(removePosition));
                    photoDataList.remove(removePosition);
                    adapter.notifyItemRemoved(removePosition);
                    adapter.notifyItemRangeChanged(removePosition, photoDataList.size());
                    photoCount.setText(String.valueOf(photoDataList.size()));
                }
            }
        });

        createContentRecyclerView.setAdapter(adapter);


    }

    ActivityResultLauncher<Intent> launcher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == RESULT_OK) {
                        Log.d(TAG, "result: " + result);
                        Intent intent = result.getData();

                        Log.d(TAG, "intent: " + intent);
                        photo = intent.getData();

                        if (photo != null) {
                            PhotoData data = new PhotoData(photo);
                            photoDataList.add(data);
                            createContentRecyclerView.setAdapter(adapter);
                            Log.d(TAG, "이미지 추가");
                            //cameraImage.setVisibility(View.VISIBLE);
                            photo = null;
                            //photoImage.setImageBitmap(null);
                            photoCount.setText(String.valueOf(photoDataList.size()));
                        }

                        Log.d(TAG, "uri: " + photo);

//                        Glide.with(CreateContentActivity.this)
//                                .load(photo)
//                                .into(photoImage);

                        // cameraImage.setVisibility(View.GONE);
                    }
                }
            });


}