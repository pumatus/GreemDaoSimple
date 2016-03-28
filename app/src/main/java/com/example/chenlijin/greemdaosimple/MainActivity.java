package com.example.chenlijin.greemdaosimple;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

import com.example.chenlijin.greemdaosimple.adapter.NoteListAdapter;
import com.example.chenlijin.greemdaosimple.dao.DaoMaster;
import com.example.chenlijin.greemdaosimple.dao.DaoSession;
import com.example.chenlijin.greemdaosimple.dao.Note;
import com.example.chenlijin.greemdaosimple.dao.NoteDao;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity implements NoteListAdapter.OnItemClickListener{

    @Bind(R.id.edittext_content)
    EditText edittextContent;
    @Bind(R.id.button_add)
    Button buttonAdd;
    @Bind(R.id.recyclerview)
    RecyclerView recyclerview;
    private DaoMaster mDaoMaster;
    private DaoSession mDaoSession;
    private NoteDao mNoteDao;
    private NoteListAdapter mAdapter;
    private List<Note> noteList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        //创数据库
        DaoMaster.DevOpenHelper db = new DaoMaster.DevOpenHelper(this, "notes-db", null);
        //获取daomaster
        mDaoMaster = new DaoMaster(db.getWritableDatabase());
        //创建一个会话
        mDaoSession = mDaoMaster.newSession();
        //获取NoteDao
        mNoteDao = mDaoSession.getNoteDao();
        initData();
        initViews();
    }

    private void initData() {
        //Todo 查询
        noteList = mNoteDao.queryBuilder().list();
    }

    private void initViews() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        recyclerview.setLayoutManager(layoutManager);
        mAdapter = new NoteListAdapter(noteList,this);
        recyclerview.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(this);
    }

    @OnClick(R.id.button_add)
    public void onClick() {
        addNote();
    }

    private void addNote() {
        String noteText = edittextContent.getText().toString();
        edittextContent.setText("");
        final DateFormat df = DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.MEDIUM);
        String comment = "添加于:" + df.format(new Date());
        //id可以为空
        Note note = new Note(null, noteText, comment, new Date());
        //Todo 添加
        mNoteDao.insert(note);
        Log.d("DaoExample", "Inserted new note, ID: " + note.getId());
        refreshNoteList();
    }

    private void refreshNoteList() {
        noteList = mNoteDao.queryBuilder().list();
        mAdapter.setNoteList(noteList);
    }

    @Override
    public void onItemClick(int position) {
        mNoteDao.delete(noteList.get(position));
        refreshNoteList();
    }
}
