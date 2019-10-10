package com.example.testfinal3

import android.content.ContentValues
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_add_student.*
import java.lang.Exception

class AddStudent : AppCompatActivity() {

    var id = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_student)

        try {
            val bundle:Bundle = intent.extras!!
            id = bundle.getInt("ID",0)
            if (id!=0){
                stu_name_ed.setText(bundle.getString("studentname"))
                stu_id_ed.setText(bundle.getString("studentid"))
            }
        }catch (ex:Exception){}

    }

    fun addStudent(view: View){
        val dbManager = DbStudent(this)
        val values = ContentValues()
        values.put("Name", stu_name_ed.text.toString())
        values.put("StudentId", stu_id_ed.text.toString())

        if (id ==0){
            val ID = dbManager.insert(values)
            if (ID > 0){
                Toast.makeText(this, "Student is added", Toast.LENGTH_SHORT).show()
                finish()
            }
            else{
                Toast.makeText(this, "Error adding student...", Toast.LENGTH_SHORT).show()
            }
        }
        else{
            val selectionArgs = arrayOf(id.toString())
            val ID = dbManager.update(values, "ID=?", selectionArgs)
            if (ID>0){
                Toast.makeText(this, "Student is added", Toast.LENGTH_SHORT).show()
                finish()
            }
            else{
                Toast.makeText(this, "Error adding student...", Toast.LENGTH_SHORT).show()
            }
        }
    }
}