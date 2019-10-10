package com.example.testfinal3

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.student_detail.view.*

class MainActivity : AppCompatActivity() {

    var listNotes = ArrayList<Student>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        LoadQuery("%")
    }

    override fun onResume() {
        super.onResume()
        LoadQuery("%")
    }

    private fun LoadQuery(title: String) {
        val dbManager = DbStudent(this)
        val projections = arrayOf("ID", "StudentName", "StudentId")
        val selectionArgs = arrayOf(title)
        val cursor = dbManager.Query(projections, "StudentName like ?", selectionArgs, "StudentsName")
        listNotes.clear()
        if (cursor.moveToFirst()) {
            do {
                val ID = cursor.getInt(cursor.getColumnIndex("ID"))
                val StudentName = cursor.getString(cursor.getColumnIndex("StudentName"))
                val StudentId = cursor.getString(cursor.getColumnIndex("StudentId"))
                listNotes.add(Student(ID,StudentName,StudentId))

            } while (cursor.moveToNext())
        }

        val myNotesAdapter = MyNotesAdapter(this, listNotes)
        stuLv.adapter = myNotesAdapter

        val total = stuLv.count
        val mActionBar = supportActionBar
        if (mActionBar != null) {
            //set to actionbar as subtitle of actionbar
            mActionBar.subtitle = "You have $total students in list..."
        }
    }
    inner class MyNotesAdapter(context: Context, private var listStudentAdapter: ArrayList<Student>) :
        BaseAdapter() {
        private var context: Context? = context

        @SuppressLint("ViewHolder", "InflateParams")
        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
            //inflate layout row.xml
            val myView = layoutInflater.inflate(R.layout.student_detail, null)
            val myNote = listStudentAdapter[position]
            myView.student_nameTv.text = myNote.studentName
            myView.student_IdTv.text = myNote.studentId
            //delete button click
            myView.deleteBtn.setOnClickListener {
                val dbManager = DbStudent(this.context!!)
                val selectionArgs = arrayOf(myNote.nodeID.toString())
                dbManager.delete("ID=?", selectionArgs)
                LoadQuery("%")
            }
            //edit//update button click
            myView.editBtn.setOnClickListener {
                GoToUpdateFun(myNote)
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
        intent.putExtra("ID", myStudent.nodeID) //put id
        intent.putExtra("studentname", myStudent.studentName) //ut name
        intent.putExtra("studentid", myStudent.studentId) //put description
        startActivity(intent) //start activity
    }
}