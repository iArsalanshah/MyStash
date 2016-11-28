package com.citemenu.mystash.home;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.citemenu.mystash.R;
import com.citemenu.mystash.dialog_fragments.DatePickerFragment;
import com.citemenu.mystash.helper.GridSpacingItemDecoration;
import com.citemenu.mystash.home.upload_bill_camera.TakePhoto;
import com.citemenu.mystash.pojo.pojo_login.Users;
import com.citemenu.mystash.pojo.upload_bills.UploadBills;
import com.citemenu.mystash.utils.CustomSharedPref;
import com.citemenu.mystash.utils.DateUtils;
import com.citemenu.mystash.utils.Utils;
import com.citemenu.mystash.webservicefactory.WebServicesFactory;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.OnShowRationale;
import permissions.dispatcher.PermissionRequest;
import permissions.dispatcher.RuntimePermissions;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@RuntimePermissions
public class AddBillDetails extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, View.OnClickListener {

    //    private static final int REQ_PIC_TOP = 123;
//    private static final int REQ_PIC_MIDDLE = 321;
//    private static final int REQ_PIC_BOTTOM = 122;
    private static final int REQ_MULTIPLE_PICS = 112;
    private static String dateFormat = "dd/MMM/yyyy";
    private static String textBtn;
    private EditText etDate, etName, etInvoiceNo, etAmount;
    private Spinner spinnerBillType;
    private Button btnUploadBill;
    //    private ImageView img1, img2, img3;
//    private Bitmap bmBill1, bmBill3;
//    private Bitmap[] bmLongBill = new Bitmap[100];
    private List<Bitmap> bmLongBill;
    //    private GridView gridThumbnail;
    private RecyclerView rvThumbnails;
    private ThumbnailsAdapter adapter;
    private Users userObj;
    private int billType;
    private ProgressDialog dialog;
    private Map<String, RequestBody> map = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_bill_details);
        init();
        etDate.setOnClickListener(this);
        btnUploadBill.setOnClickListener(this);
        userObj = CustomSharedPref.getUserObject(this);
//        img1.setOnClickListener(this);
//        img2.setOnClickListener(this);
//        img3.setOnClickListener(this);
    }

    private void init() {
        etDate = (EditText) findViewById(R.id.add_bill_date);
        etName = (EditText) findViewById(R.id.et_restaurantName);
        etInvoiceNo = (EditText) findViewById(R.id.et_restaurantInvoiceNo);
        etAmount = (EditText) findViewById(R.id.et_restaurantAmount);

        spinnerBillType = (Spinner) findViewById(R.id.sp_restaurantBillType);

        rvThumbnails = (RecyclerView) findViewById(R.id.recycler_thumbnails);
        adapter = new ThumbnailsAdapter(this);
        bmLongBill = new ArrayList<>();
        btnUploadBill = (Button) findViewById(R.id.buttonUploadBill);
//        img1 = (ImageView) findViewById(R.id.img_UploadBill1);
//        img2 = (ImageView) findViewById(R.id.img_UploadBill2);
//        img3 = (ImageView) findViewById(R.id.img_UploadBill3);

        etDate.setText(Utils.dateToString(new Date(), dateFormat));
        rvThumbnails.addItemDecoration(new GridSpacingItemDecoration(3, 50, false));
        rvThumbnails.setLayoutFrozen(true);
        rvThumbnails.setLayoutManager(new GridLayoutManager(this, 3, GridLayoutManager.VERTICAL, false));
        rvThumbnails.setAdapter(adapter);

        spinnerBillType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                billType = position + 1;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        dialog = new ProgressDialog(this);
        dialog.setCancelable(false);
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setMessage("Please wait...");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.add_bill_date:
                // Show date picker
                showDatePickerDialog(v);
                break;
            case R.id.buttonUploadBill:
                if (btnUploadBill.getText().toString().equals("Upload Bill")) {
                    dialog.show();
                    map = getCompressedBitmaps(bmLongBill);
                    uploadBills();
                } else {
                    AddBillDetailsPermissionsDispatcher.showCameraWithCheck(this);
                }
                break;
            default:
                break;
        }
    }

    private void uploadBills() {
        if (!etName.getText().toString().isEmpty() &&
                !etAmount.getText().toString().isEmpty() &&
                !etDate.getText().toString().isEmpty() &&
                !etInvoiceNo.getText().toString().isEmpty()) {

            Call<UploadBills> call = WebServicesFactory.getInstance().uploadBills("upload_bills", userObj.getId(), etName.getText().toString(),
                    etDate.getText().toString(), etName.getText().toString(),
                    etAmount.getText().toString(), String.valueOf(billType), etInvoiceNo.getText().toString(), map);
            call.enqueue(new Callback<UploadBills>() {
                @Override
                public void onResponse(Call<UploadBills> call, Response<UploadBills> response) {
                    dialog.dismiss();
                    UploadBills object = response.body();
                    if (object == null) {
                        Toast.makeText(AddBillDetails.this, "Found Null", Toast.LENGTH_SHORT).show();
                    } else if (object.getHeader().getSuccess().equals("1")) {
                        Toast.makeText(AddBillDetails.this, "Bill Uploaded", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(AddBillDetails.this, object.getHeader().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<UploadBills> call, Throwable t) {
                    dialog.dismiss();
                    Log.d("TAG MULTIPLE IMAGES", " onFailure: " + t);
                    Toast.makeText(AddBillDetails.this, "Something went wrong. Please try again", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            dialog.dismiss();
            Toast.makeText(AddBillDetails.this, "Please complete all required fields", Toast.LENGTH_SHORT).show();
        }
    }

    private Map<String, RequestBody> getCompressedBitmaps(List<Bitmap> bmLongBill) {
        Map<String, RequestBody> map = new HashMap<>();

        for (int i = 0; i < bmLongBill.size(); i++) {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            bmLongBill.get(i).compress(Bitmap.CompressFormat.JPEG, 50, byteArrayOutputStream);
            // create RequestBody instance from file
            long time= System.currentTimeMillis();
            RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), byteArrayOutputStream.toByteArray());
            // MultipartBody.Part is used to send also the actual file name
//            MultipartBody.Part body = MultipartBody.Part.createFormData("uploaded_file[]", "upload_image", requestFile);
            map.put("uploaded_file[]\"; filename=\"" + time + "img.jpg\"", requestFile);
        }
        return map;
    }

    private void startCameraActivity(int reqCode, String type) {
        Intent takePictureIntent = new Intent(AddBillDetails.this, TakePhoto.class);
//        takePictureIntent.putExtra("billType", type);
        startActivityForResult(takePictureIntent, reqCode);
    }

    public void showDatePickerDialog(View v) {
        DatePickerFragment datePickerFragment = new DatePickerFragment();
        datePickerFragment.setOnDateSetListener(this);
        datePickerFragment.show(getSupportFragmentManager(), "DatePickerFragment");
    }

    public void onBackButtonClicked(View view) {
        this.finish();
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int day) {
        // Do something with the date chosen by the user
        String strDate = String.valueOf(day)
                + "/" + String.valueOf(month + 1)
                + "/" + String.valueOf(year);
        Date pickerDate = Utils.stringToDate(strDate, "dd/MM/yyyy");
        if (!DateUtils.isAfterDay(pickerDate, new Date())) {
            etDate.setText(Utils.dateToString(pickerDate, dateFormat));
        } else {
            Utils.showShortToastInCenter(this, "Selected date is not valid");
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == REQ_PIC_TOP) {
//            if (resultCode == RESULT_OK) {
//                bmBill1 = AddBillsCamera.bmRestrntName;
//                if (bmBill1 == null) {
//                    Toast.makeText(this, "null found", Toast.LENGTH_SHORT).show();
//                } else {
//                    img1.setImageResource(R.drawable.tick_green_upload_bills);
//                }
//            }
//        } else
        if (requestCode == REQ_MULTIPLE_PICS) {
            if (resultCode == RESULT_OK) {
//                if (bmLongBill == null) {
//                    Toast.makeText(this, "No Image Found", Toast.LENGTH_SHORT).show();
//                } else {
                bmLongBill = getBitmapsFromGallery(TakePhoto.arrFile);
                adapter.notifyDataSetChanged();
                btnUploadBill.setText("Upload Bill");
//                finalImages = getCompressedBitmaps(bmLongBill);
//                    img2.setImageResource(R.drawable.tick_green_upload_bills);
//                }
            }
        }
//        else if (requestCode == REQ_PIC_BOTTOM) {
//            if (resultCode == RESULT_OK) {
//                bmBill3 = AddBillsCamera.bmAmount;
//                if (bmBill3 == null) {
//                    Toast.makeText(this, "null found", Toast.LENGTH_SHORT).show();
//                } else
//                    img3.setImageResource(R.drawable.tick_green_upload_bills);
//            }
//        }
    }

    private List<Bitmap> getBitmapsFromGallery(ArrayList<String> arrFile) {
        List<Bitmap> bitmap = new ArrayList<>();
        for (int i = 0; i < arrFile.size(); i++) {
            Bitmap bm = BitmapFactory.decodeFile(arrFile.get(i));
            if (bm != null)
                bitmap.add(bm);
            File file = new File(arrFile.get(i));
            if (file.exists()) {
                file.delete();
            }
        }
        return bitmap;
    }

    private class ThumbnailsAdapter extends RecyclerView.Adapter<ThumbnailsAdapter.VH> {
        private Context context;

        ThumbnailsAdapter(Context context) {
            this.context = context;
        }

        @Override
        public VH onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(context).inflate(R.layout.item_bill_thumbnails, parent, false);
            return new VH(itemView);
        }

        @Override
        public void onBindViewHolder(VH holder, int position) {
            holder.img_item_billThumbnail.setImageBitmap(bmLongBill.get(position));
        }

        @Override
        public int getItemCount() {
            return bmLongBill.size();
        }

        class VH extends RecyclerView.ViewHolder {
            ImageView img_item_billThumbnail;

            public VH(View itemView) {
                super(itemView);
                img_item_billThumbnail = (ImageView) itemView.findViewById(R.id.img_item_billThumbnail);
            }
        }
    }

    @NeedsPermission({Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE})
    void showCamera() {
        startCameraActivity(REQ_MULTIPLE_PICS, "middle");
    }

    @OnShowRationale({Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE})
    void showRationaleForCamera(final PermissionRequest request) {
        new AlertDialog.Builder(this)
                .setMessage("Upload bill feature required Camera and Storage Permission")
                .setPositiveButton("Allow", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        request.proceed();
                    }
                })
                .setNegativeButton("Deny", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        request.cancel();
                    }
                })
                .show();
    }


    @OnPermissionDenied({Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE})
    void showDeniedForCamera() {
        Toast.makeText(this, "Upload bill feature can not completed without Camera and Storage Access", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        // NOTE: delegate the permission handling to generated method
        AddBillDetailsPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }
}