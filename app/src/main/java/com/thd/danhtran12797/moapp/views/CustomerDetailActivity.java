package com.thd.danhtran12797.moapp.views;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.thd.danhtran12797.moapp.R;
import com.thd.danhtran12797.moapp.adapters.AddressAdapter;
import com.thd.danhtran12797.moapp.databinding.ActivityDetailCustomerBinding;
import com.thd.danhtran12797.moapp.databinding.DialogSelectAddressBinding;
import com.thd.danhtran12797.moapp.models.AddressDetail;
import com.thd.danhtran12797.moapp.models.Customer;
import com.thd.danhtran12797.moapp.models.ListProvin;
import com.thd.danhtran12797.moapp.viewmodels.AddressViewModel;
import com.thd.danhtran12797.moapp.viewmodels.CustomerViewModel;

import java.util.List;

import static com.thd.danhtran12797.moapp.utils.Constants.DATA_CHANGE;
import static com.thd.danhtran12797.moapp.utils.Constants.KEY_CUSTOMER;

public class CustomerDetailActivity extends BaseActivity implements View.OnClickListener, AddressAdapter.AddressInterface, View.OnFocusChangeListener {

    private static final String TAG = "CustomerDetailActivity";

    private ActivityDetailCustomerBinding detailCustomerBinding;
    private AddressViewModel addressViewModel;
    private CustomerViewModel customerViewModel;
    private Customer customer;
    private boolean isDataChange = false;
    private boolean hideMenu = false;
    private DialogSelectAddressBinding selectAddressBinding;
    private Dialog selectAddressDialog;
    private AddressAdapter addressAdapter;

    private String name, phone, classCus, city, district, ward, street;
    private int idCity = -1;
    private int idDistric = -1;
    private int idClass = 1;
    private int code = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        detailCustomerBinding = ActivityDetailCustomerBinding.inflate(getLayoutInflater());
        setContentView(detailCustomerBinding.getRoot());

        setSupportActionBar(detailCustomerBinding.customerDetailToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        detailCustomerBinding.customerDetailToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBack();
            }
        });

        if (getIntent().hasExtra(KEY_CUSTOMER)) {
            customer = (Customer) getIntent().getSerializableExtra(KEY_CUSTOMER);
            idClass = Integer.parseInt(customer.getClassCus());
            detailCustomerBinding.setCustomer(customer);
            detailCustomerBinding.setEnableView(false);
        } else {
            getSupportActionBar().setTitle("Thêm mới khách hàng");
            detailCustomerBinding.nameEditText.requestFocus();
            detailCustomerBinding.setEnableView(true);
            hideMenu = true;
        }

        addressViewModel = new ViewModelProvider(this).get(AddressViewModel.class);
        customerViewModel = new ViewModelProvider(this).get(CustomerViewModel.class);

        initAddressDialog();

        detailCustomerBinding.cityEditText.setOnClickListener(this);
        detailCustomerBinding.districtEditText.setOnClickListener(this);
        detailCustomerBinding.wardEditText.setOnClickListener(this);
        detailCustomerBinding.classEditText.setOnClickListener(this);

        detailCustomerBinding.cityEditText.setOnFocusChangeListener(this);
        detailCustomerBinding.districtEditText.setOnFocusChangeListener(this);
        detailCustomerBinding.wardEditText.setOnFocusChangeListener(this);
        detailCustomerBinding.classEditText.setOnFocusChangeListener(this);
        detailCustomerBinding.saveButton.setOnClickListener(this);

    }

    private void initAddressDialog() {
        selectAddressBinding = DataBindingUtil.inflate(LayoutInflater.from(this), R.layout.dialog_select_address, null, false);

        selectAddressDialog = new Dialog(this);
        selectAddressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        selectAddressDialog.setContentView(selectAddressBinding.getRoot());

        addressAdapter = new AddressAdapter(this);
        selectAddressBinding.recyclerSelectAddress.setAdapter(addressAdapter);

        selectAddressBinding.searchAddressDetail.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                addressAdapter.getFilter().filter(newText);
                return false;
            }
        });

        selectAddressBinding.imgCloseDialogSelectAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectAddressDialog.dismiss();
            }
        });
    }

    private void showAddressDialog(int id, int code) {
        switch (code) {
            case 1:
                load_all_provin();
                break;
            case 2:
                load_all_district(id);
                break;
            case 3:
                load_all_ward(id);
                break;
        }
        setDataDialog(code);

        selectAddressDialog.show();
    }

    private void setDataDialog(int code) {
        if (code == 1) {
            selectAddressBinding.txtTitleSelectAddress.setText("Chọn Tỉnh/Thành");
            selectAddressBinding.searchAddressDetail.setQueryHint("Tìm kiếm Tỉnh/Thành");
        } else if (code == 2) {
            selectAddressBinding.txtTitleSelectAddress.setText("Chọn Quận/Huyện");
            selectAddressBinding.searchAddressDetail.setQueryHint("Tìm kiếm Quận/Huyện");
        } else {
            selectAddressBinding.txtTitleSelectAddress.setText("Chọn Phường/Xã");
            selectAddressBinding.searchAddressDetail.setQueryHint("Tìm kiếm Phường/Xã");
        }
    }

    private void showProgress() {
        selectAddressBinding.setIsLoading(true);
    }

    private void hideProgress() {
        selectAddressBinding.setIsLoading(false);
    }

    private void load_all_provin() {
        showProgress();
        addressViewModel.getAllProvin().observe(this, new Observer<ListProvin>() {
            @Override
            public void onChanged(ListProvin listProvin) {
                hideProgress();
                if (listProvin != null) {
                    addressAdapter.submitList(listProvin.getLtsItem());
                    addressAdapter.setAddressListAll(listProvin.getLtsItem());
                } else {
                    Toast.makeText(CustomerDetailActivity.this, "Vui lòng kiểm tra Internet!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void load_all_district(int id) {
        showProgress();
        addressViewModel.getAllDistrict(id).observe(this, new Observer<List<AddressDetail>>() {
            @Override
            public void onChanged(List<AddressDetail> addressDetails) {
                hideProgress();
                if (addressDetails != null) {
                    addressAdapter.submitList(addressDetails);
                    addressAdapter.setAddressListAll(addressDetails);
                } else {
                    Toast.makeText(CustomerDetailActivity.this, "Vui lòng kiểm tra Internet!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void load_all_ward(int id) {
        showProgress();
        addressViewModel.getAllWard(id).observe(this, new Observer<List<AddressDetail>>() {
            @Override
            public void onChanged(List<AddressDetail> addressDetails) {
                hideProgress();
                if (addressDetails != null) {
                    addressAdapter.submitList(addressDetails);
                    addressAdapter.setAddressListAll(addressDetails);
                } else {
                    Toast.makeText(CustomerDetailActivity.this, "Vui lòng kiểm tra Internet!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private boolean checkPhoneValidate(String phone) {
        String[] arr = getResources().getStringArray(R.array.frist_number_phone);
        String frist_number = phone.substring(0, 3);
        for (String s : arr) {
            if (s.equals(frist_number))
                return true;
        }
        return false;
    }


    private String getFullAddress(String city, String distric, String ward, String street) {
        String full_address = (new StringBuilder())
                .append(street)
                .append(", " + ward)
                .append(", " + distric)
                .append(", " + city)
                .toString();
        return full_address;
    }

    private void extra_full_address(String address_full) {
//        String arr[] = address_full.split(", ");
//        provin = arr[3];
//        distric = arr[2];
//        ward = arr[1];
//        address = arr[0];

    }

    private boolean checkValidate() {
        detailCustomerBinding.nameTextInputLayout.setError(null);
        detailCustomerBinding.phoneTextInputLayout.setError(null);
        detailCustomerBinding.classTextInputLayout.setError(null);
        detailCustomerBinding.cityTextInputLayout.setError(null);
        detailCustomerBinding.districtTextInputLayout.setError(null);
        detailCustomerBinding.wardTextInputLayout.setError(null);
        detailCustomerBinding.streetTextInputLayout.setError(null);

        if (name.isEmpty()) {
            detailCustomerBinding.nameTextInputLayout.setError("Vui lòng nhập tên khách hàng!");
            detailCustomerBinding.nameEditText.requestFocus();
            return false;
        }

        if (phone.isEmpty()) {
            detailCustomerBinding.phoneTextInputLayout.setError("Vui lòng nhập số điện thoại!");
            detailCustomerBinding.phoneEditText.requestFocus();
            return false;
        } else if (phone.length() < 10) {
            detailCustomerBinding.phoneTextInputLayout.setError("Vui lòng nhập đầy đủ số điện thoại!");
            detailCustomerBinding.phoneEditText.requestFocus();
            return false;
        } else if (!checkPhoneValidate(phone)) {
            detailCustomerBinding.phoneTextInputLayout.setError("Vui lòng nhập đúng số điện thoại!");
            detailCustomerBinding.phoneEditText.requestFocus();
            return false;
        }

        if (classCus.isEmpty()) {
            detailCustomerBinding.classTextInputLayout.setError("Vui lòng chọn phân loại đối tác!");
            detailCustomerBinding.classEditText.requestFocus();
            return false;
        }

        if (street.isEmpty()) {
            detailCustomerBinding.streetTextInputLayout.setError("Vui lòng nhập số nhà tên đường!");
            detailCustomerBinding.streetEditText.requestFocus();
            return false;
        }

        if (city.isEmpty()) {
            detailCustomerBinding.cityTextInputLayout.setError("Vui lòng chọn Tỉnh/Thành phố!");
            detailCustomerBinding.cityEditText.requestFocus();
            return false;
        }

        if (district.isEmpty()) {
            detailCustomerBinding.districtTextInputLayout.setError("Vui lòng chọn Tỉnh/Thành phố!");
            detailCustomerBinding.districtTextInputLayout.requestFocus();
            return false;
        }

        if (ward.isEmpty()) {
            detailCustomerBinding.wardTextInputLayout.setError("Vui lòng chọn Tỉnh/Thành phố!");
            detailCustomerBinding.wardEditText.requestFocus();
            return false;
        }

        return true;
    }

    private void showDeleteDialog() {
        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(this);
        builder.setTitle("Xóa khách hàng");
        builder.setMessage("Bạn có xác nhận xóa khách hàng này?");
        builder.setIcon(R.drawable.ic_baseline_warning);
        builder.setCancelable(false);
        builder.setPositiveButton("Xác nhận", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                deletecustomer();
            }
        });
        builder.setNegativeButton("Hủy", null);
        builder.show();
    }

    private void deletecustomer() {
        detailCustomerBinding.setIsLoading(true);
        customerViewModel.deleteCustomer(customer.getId()).observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                if (s != null) {
                    Toast.makeText(CustomerDetailActivity.this, "Xóa khách hàng thành công", Toast.LENGTH_SHORT).show();
                    isDataChange = true;
                    onBack();
                } else
                    Toast.makeText(CustomerDetailActivity.this, "Vui lòng kiểm tra Internet!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateCustomer() {
        detailCustomerBinding.setIsLoading(true);
        customerViewModel.updateCustomer(customer.getId(), name, phone, getFullAddress(city, district, ward, street), classCus)
                .observe(this, new Observer<String>() {
                    @Override
                    public void onChanged(String s) {
                        if (s != null) {
                            Toast.makeText(CustomerDetailActivity.this, "Cập nhật khách hàng thành công", Toast.LENGTH_SHORT).show();
                            detailCustomerBinding.setIsLoading(false);
                            detailCustomerBinding.setEnableView(false);
                            isDataChange = true;
                        } else
                            Toast.makeText(CustomerDetailActivity.this, "Vui lòng kiểm tra Internet!", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void insertCustomer() {
        detailCustomerBinding.setIsLoading(true);
        customerViewModel.insertCustomer(name, phone, getFullAddress(city, district, ward, street), classCus)
                .observe(this, new Observer<String>() {
                    @Override
                    public void onChanged(String s) {
                        if (s != null) {
                            Toast.makeText(CustomerDetailActivity.this, "Thêm mới khách hàng thành công", Toast.LENGTH_SHORT).show();
                            isDataChange = true;
                            onBack();
                        } else
                            Toast.makeText(CustomerDetailActivity.this, "Vui lòng kiểm tra Internet!", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.customer_menu, menu);
        if (hideMenu) {
            for (int i = 0; i < menu.size(); i++)
                menu.getItem(i).setVisible(false);
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_edit_customer:
                detailCustomerBinding.setEnableView(true);
                break;
            case R.id.menu_delete_customer:
                showDeleteDialog();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        onBack();
    }

    public void onBack() {
        Intent intent = new Intent();
        intent.putExtra(DATA_CHANGE, isDataChange);
        setResult(RESULT_OK, intent);
        finish();
    }

    private void clickCity() {
        code = 1;
        showAddressDialog(-1, code);
        detailCustomerBinding.districtEditText.setText("");
        detailCustomerBinding.wardEditText.setText("");
    }

    private void clickDistrict() {
        code = 2;
        if (idCity != -1) {
            showAddressDialog(idCity, code);
            detailCustomerBinding.wardEditText.setText("");
        } else {
            Toast.makeText(this, "Vui lòng chọn tỉnh/thành", Toast.LENGTH_SHORT).show();
        }
    }

    private void clickWard() {
        code = 3;
        if (idDistric != -1) {
            showAddressDialog(idDistric, code);
        } else {
            Toast.makeText(this, "Vui lòng chọn quận/huyện", Toast.LENGTH_SHORT).show();
        }
    }

    private void clickClass() {
        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(this);
        builder.setTitle("Phân loại đối tác");
        builder.setSingleChoiceItems(new String[]{"Cá nhân", "Doanh nghiệp"}, idClass, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                closeKeyboard();
                idClass = i;
                detailCustomerBinding.classEditText.setText(i == 0 ? "Cá nhân" : "Doanh nghiệp");
                dialogInterface.dismiss();
            }
        });
        builder.setCancelable(false);
        builder.show();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.cityEditText:
                clickCity();
                break;
            case R.id.districtEditText:
                clickDistrict();
                break;
            case R.id.wardEditText:
                clickWard();
                break;
            case R.id.classEditText:
                clickClass();
                break;
            case R.id.saveButton:
                name = detailCustomerBinding.nameEditText.getText().toString().trim();
                phone = detailCustomerBinding.phoneEditText.getText().toString().trim();
                classCus = detailCustomerBinding.classEditText.getText().toString().trim();
                city = detailCustomerBinding.cityEditText.getText().toString().trim();
                district = detailCustomerBinding.districtEditText.getText().toString().trim();
                ward = detailCustomerBinding.wardEditText.getText().toString().trim();
                street = detailCustomerBinding.streetEditText.getText().toString().trim();
                if (checkValidate()) {
                    if (getIntent().hasExtra(KEY_CUSTOMER)) {
                        updateCustomer();
                    } else {
                        insertCustomer();
                    }
                }
                break;
        }
    }

    @Override
    public void onItemClick(AddressDetail addressDetail) {
        if (code == 1) {
            detailCustomerBinding.cityEditText.setText(addressDetail.getTitle());
            idCity = addressDetail.getId();
        } else if (code == 2) {
            detailCustomerBinding.districtEditText.setText(addressDetail.getTitle());
            idDistric = addressDetail.getId();
        } else {
            detailCustomerBinding.wardEditText.setText(addressDetail.getTitle());
        }
        selectAddressDialog.dismiss();
    }

    @Override
    public void onFocusChange(View view, boolean b) {
        if (view.hasFocus()) {
            switch (view.getId()) {
                case R.id.cityEditText:
                    clickCity();
                    break;
                case R.id.districtEditText:
                    clickDistrict();
                    break;
                case R.id.wardEditText:
                    clickWard();
                    break;
                case R.id.classEditText:
                    clickClass();
                    break;
            }
        }
    }
}