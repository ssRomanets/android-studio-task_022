package com.example.task_022

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import kotlin.system.exitProcess

class MainActivity : AppCompatActivity() {

    private var personFirstName = ""
    private var personLastName = ""
    private var personAge = ""
    private var personAppointment = ""
    private var selectAppointment = ""

    var persons: MutableList<Person> = mutableListOf()
    var listAdapter: ListAdapter? = null

    private val role = mutableListOf(
        "appointment",
        "Engineer",
        "Designer",
        "Contractor",
        "Master",
        "PowerEngineer",
        "Mechanic"
    )

    private lateinit var listViewLV: ListView
    private lateinit var toolbarMain: Toolbar
    private lateinit var personFirstNameET: EditText
    private lateinit var personLastNameET: EditText
    private lateinit var personAgeET: EditText
    private lateinit var spinner: Spinner
    private lateinit var saveBTN: Button
    private lateinit var autoCompleteTV: AutoCompleteTextView

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        init()

        setSupportActionBar(toolbarMain)
        title = "Подбор персонала."

        saveBTN.setOnClickListener{
            createPerson()
            listAdapter = ListAdapter(this@MainActivity, persons)
            listViewLV.adapter = listAdapter
            listAdapter?.notifyDataSetChanged()
            clearEditFields()
        }

        //adapter Spinner
        val adapterSpinner = ArrayAdapter(this, android.R.layout.simple_spinner_item, role)
        adapterSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapterSpinner

        val itemSelectedListener: AdapterView.OnItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                    val item = parent?.getItemAtPosition(position) as String
                    personAppointment = item.toString()
                }
                override  fun onNothingSelected(parent: AdapterView<*>?) {}
            }
        spinner.onItemSelectedListener = itemSelectedListener

        //adapter AutoComplete
        val adapterAutoComplete = ArrayAdapter(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, role)
        adapterAutoComplete.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        autoCompleteTV.setAdapter(adapterAutoComplete)

        val itemClickListener: AdapterView.OnItemClickListener =
            object : AdapterView.OnItemClickListener {
                override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                    val item = parent?.getItemAtPosition(position) as String
                    selectAppointment = item.toString()

                    if (selectAppointment == "") listAdapter = ListAdapter(this@MainActivity, persons)
                    else listAdapter = ListAdapter(this@MainActivity,
                        persons.filter { it.appointment == selectAppointment }.toMutableList()
                    )

                    listViewLV.adapter = listAdapter
                    listAdapter?.notifyDataSetChanged()
                }
            }
        autoCompleteTV.onItemClickListener = itemClickListener
    }

    private fun init() {
        listViewLV = findViewById(R.id.listViewLV)
        toolbarMain = findViewById(R.id.toolbarMain)
        personFirstNameET = findViewById(R.id.personFirstNameET)
        personLastNameET = findViewById(R.id.personLastNameET)
        personAgeET = findViewById(R.id.personAgeET)
        spinner = findViewById(R.id.spinner)
        autoCompleteTV = findViewById(R.id.autoCompleteTV)
        saveBTN = findViewById(R.id.saveBTN)
    }

    private fun createPerson() {
        personFirstName = personFirstNameET.text.toString()
        personLastName = personLastNameET.text.toString()
        personAge = personAgeET.text.toString()
        val person = Person(personFirstName, personLastName, personAge, personAppointment)
        persons.add(person)
    }

    private fun clearEditFields() {
        personFirstNameET.text.clear()
        personLastNameET.text.clear()
        personAgeET.text.clear()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return  true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.exitMenuMain->{
                moveTaskToBack(true);
                exitProcess(-1)
            }
        }
        return super.onOptionsItemSelected(item)
    }
}