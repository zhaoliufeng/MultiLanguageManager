package com.we_smart.test;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.we_smart.sqldao.DBHelper;
import com.we_smart.test.model.SqlTestBean;

import java.util.ArrayList;
import java.util.List;

public class SqlActivity extends AppCompatActivity {

    private EditText mEdtId;
    private EditText mEdtName;
    private EditText mEdtGroupId;
    private RecyclerView mListView;
    private ListAdapter listAdapter;
    private BeanDAO dao;
    private List<SqlTestBean> data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sql);
        mEdtId = findViewById(R.id.edt_id);
        mEdtName = findViewById(R.id.edt_name);
        mEdtGroupId = findViewById(R.id.edt_group_id);

        mListView = findViewById(R.id.list_view);
        data = new ArrayList<>();
        listAdapter = new ListAdapter(this, data);
        mListView.setAdapter(listAdapter);
        mListView.setLayoutManager(new LinearLayoutManager(this));

//        DBHelper.getInstance().initDBHelper(new DBOpenHelper(this, "test.db"));
        dao = new BeanDAO();
    }

    public void onInsertClick(View view) {
//        if (dao.insert(packData())) {
//            toast("插入成功");
//        }
    }

    public void onDeleteClick(View view) {
//        if (dao.delete(packData())) {
//            toast("删除成功");
//        }
    }

    public void onUpdateClick(View view) {
//        if (dao.update(packData())) {
//            toast("更新成功");
//        }
    }

    private void toast(String s) {
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
    }

    private SqlTestBean packData() {
        SqlTestBean bean = new SqlTestBean();
        bean.id = Integer.parseInt(mEdtId.getText().toString().trim());
        bean.name = mEdtName.getText().toString().trim();
        bean.groupId = mEdtGroupId.getText().toString().trim().isEmpty() ?
                0 : Integer.parseInt(mEdtGroupId.getText().toString().trim());
        return bean;
    }

    public void onQueryClick(View view) {
        List<SqlTestBean> list;
        if (mEdtGroupId.getText().toString().trim().isEmpty()) {
//            list = dao.query();
        } else {
            list = dao.getListById(Integer.parseInt(mEdtId.getText().toString().trim()),
                    Integer.parseInt(mEdtGroupId.getText().toString().trim()));
        }
        data.clear();
//        data.addAll(list);
        listAdapter.notifyDataSetChanged();
    }

    private class ListAdapter extends RecyclerView.Adapter {

        private Context mContext;
        private List<SqlTestBean> mData;

        ListAdapter(Context mContext, List<SqlTestBean> list) {
            this.mContext = mContext;
            this.mData = list;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.list_item, null);
            return new ListViewHolder(view);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            ListViewHolder viewHolder = (ListViewHolder) holder;
            viewHolder.mTvName.setText(mData.get(position).name);
            viewHolder.mTvId.setText(String.valueOf(mData.get(position).id));
            viewHolder.mTvGroupId.setText(String.valueOf(mData.get(position).groupId));
        }

        @Override
        public int getItemCount() {
            return mData.size();
        }

        private class ListViewHolder extends RecyclerView.ViewHolder {
            TextView mTvId;
            TextView mTvName;
            TextView mTvGroupId;

            ListViewHolder(View itemView) {
                super(itemView);
                mTvId = itemView.findViewById(R.id.tv_id);
                mTvName = itemView.findViewById(R.id.tv_name);
                mTvGroupId = itemView.findViewById(R.id.tv_group_id);
            }
        }
    }
}
