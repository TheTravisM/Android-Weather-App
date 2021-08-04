package com.tm.shine

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.VolleyError
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import org.json.JSONException
import org.json.JSONObject

class MainActivity : AppCompatActivity() {

    private val url = "http://api.weatherstack.com/current"
    private val appid = "2093b02f759229c1da497533ba4e672a"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

//     fun ctof(num) {
//         f = num*9/5+32
//         return f
//     }


    fun getWeatherDetails(view: View) {
        var tempUrl = "http://api.weatherstack.com/current?access_key=2093b02f759229c1da497533ba4e672a&query=cincinnati"
        val etCity = findViewById<EditText>(R.id.etCity)
        val etCountry = findViewById<EditText>(R.id.etCountry)
        val city = etCity.text.toString().trim()
        val country = etCountry.text.toString().trim()
        val tvResult = findViewById<TextView>(R.id.tvResult)
        val queue = Volley.newRequestQueue(this)

        if (city == "") {
            tvResult.text = "City cannot be empty"
        } else {
            tempUrl = if (country != "") {
                "$url?access_key=$appid&query=$city"
            } else {
                "$url?access_key=$appid&query=$city"
            }

            val stringRequest = StringRequest(Request.Method.POST, tempUrl,
                { response ->
                     Log.d("response", response)
                    //String output = "";
                    var output = String()
                    try {
                        val jsonResponse = JSONObject(response)
                        val jsonObjectLocation = jsonResponse.getJSONObject("location")
                        val city = jsonObjectLocation.getInt("city").toString()
                        val jsonObjectCurrent = jsonResponse.getJSONObject("current")
                        val temp = jsonObjectCurrent.getInt("temperature").toString()
                        val feelslike = jsonObjectCurrent.getInt("feelslike").toString()
                        val humidity = jsonObjectCurrent.getInt("humidity").toString()
                        val precip = jsonObjectCurrent.getInt("precip").toString()

                        output += """Current Weather in $city
                        Temp: $temp
                        Feels Like: $feelslike
                        Humidity: $humidity
                        Precipitation: $precip"""

                        tvResult.text = output

                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                },
                {
                    Toast.makeText(applicationContext,
                        VolleyError().toString().trim(),
                        Toast.LENGTH_SHORT).show()
                })
            // Add the request to the RequestQueue.
            queue.add(stringRequest)
        }
    }
}