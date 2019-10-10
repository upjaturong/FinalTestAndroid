package com.example.testfinal3

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.student_detail.view.*

const val TAG = "MainActivity"
class MainActivity : AppCompatActivity() {

    var listNotes = ArrayList<Student>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Log.d(TAG,"onCreate: start")

        LoadQuery("%")

    }

    override fun onResume() {
        super.onResume()
        LoadQuery("%")
    }

    private fun LoadQuery(title: String) {
        Log.d(TAG,"onLoadQuery: start")
        val dbStudent = DbStudent(this)
        val projections = arrayOf("ID", "Name", "StudentId")
        Log.d(TAG,"onLoadQuery: projections")
        val selectionArgs = arrayOf(title)
        val cursor = dbStudent.Query(projections, "Name like ?", selectionArgs, "Name")
        Log.d(TAG,"onLoadQuery: cursor")
        listNotes.clear()
        if (cursor.moveToFirst()) {
            do {
                val ID = cursor.getInt(cursor.getColumnIndex("ID"))
                val StudentName = cursor.getString(cursor.getColumnIndex("Name"))
                val StudentId = cursor.getString(cursor.getColumnIndex("StudentId"))
                listNotes.add(Student(ID,StudentName,StudentId))
            } while (cursor.moveToNext())
        }

        val myNotesAdapter = MyNotesAdapter(this, listNotes)
        stuLv.adapter = myNotesAdapter

        val total = stuLv.count
        val mActionBar = supportActionBar
        if (mActionBar != null) {
            mActionBar.subtitle = "You have $total students in list..."
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu,menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item != null) {
            when (item.itemId) {
                R.id.addstudent -> {
                    startActivity(Intent(this, AddStudent::class.java))
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }

    inner class MyNotesAdapter(context: Context, private var listStudentAdapter: ArrayList<Student>) :
        BaseAdapter() {
        private var context: Context? = context
        @SuppressLint("ViewHolder", "InflateParams")
        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
            val myView = layoutInflater.inflate(R.layout.student_detail, null)
            val myStudent = listStudentAdapter[position]
            myView.student_nameTv.text = myStudent.studentName
            myView.student_IdTv.text = myStudent.studentId
            myView.deleteBtn.setOnClickListener {
                val dbManager = DbStudent(this.context!!)
                val selectionArgs = arrayOf(myStudent.nodeID.toString())
                dbManager.delete("ID=?", selectionArgs)
                LoadQuery("%")
            }
            myView.editBtn.setOnClickListener {
                GoToUpdateFun(myStudent)
            }
            return myView
        }

        override fun getItem(position: Int): Any {
            return listStudentAdapter[position]
        }

        override fun getItemId(position: Int): Long {
            return position.toLong()
        }

        override fun getCount(): Int {
            return listStudentAdapter.size
        }
    }

    private fun GoToUpdateFun(myStudent: Student) {
        val intent = Intent(this, AddStudent::class.java)
        intent.putExtra("ID", myStudent.nodeID)
        intent.putExtra("studentname", myStudent.studentName)
        intent.putExtra("studentid", myStudent.studentId)
        startActivity(intent)
    }
}