package com.sewon.officehealth.service.ble;

import android.bluetooth.BluetoothDevice;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;

public class BLEDeviceListAdapter extends BaseAdapter {
  private ArrayList<BluetoothDevice> mLeDevices;
  private LayoutInflater mInflator;

  public BLEDeviceListAdapter() {
    super();
    mLeDevices = new ArrayList<BluetoothDevice>();
//        mInflator = DeviceScanActivity.this.getLayoutInflater();
  }

  public void addDevice(BluetoothDevice device) {
    if (!mLeDevices.contains(device)) {
      mLeDevices.add(device);
    }
  }

  public BluetoothDevice getDevice(int position) {
    return mLeDevices.get(position);
  }

  public void clear() {
    mLeDevices.clear();
  }

  @Override
  public int getCount() {
    return mLeDevices.size();
  }

  @Override
  public Object getItem(int i) {
    return mLeDevices.get(i);
  }

  @Override
  public long getItemId(int i) {
    return i;
  }

  @Override
  public View getView(int position, View convertView, ViewGroup parent) {
    return null;
  }
}