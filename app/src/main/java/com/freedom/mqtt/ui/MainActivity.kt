package com.freedom.mqtt.ui

import android.os.Bundle
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.freedom.mqtt.R
import com.freedom.mqtt.useradapter
import com.freedom.mqtt.utils.logs
import com.freedom.mqtt.utils.toast
import kotlinx.android.synthetic.main.activity_main.*
import org.eclipse.paho.android.service.MqttAndroidClient
import org.eclipse.paho.client.mqttv3.*
import java.io.UnsupportedEncodingException


class MainActivity : AppCompatActivity() {
lateinit var topic:EditText
    lateinit var useradapter: useradapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        client()
        connect()

    }

    fun connect(){
        fab.setOnClickListener {
            setupmttq()
        }
    }

    fun client():MqttAndroidClient{
        val clientId = MqttClient.generateClientId()
        return MqttAndroidClient(
                this.applicationContext, "tcp://broker.hivemq.com:1883",
                clientId
        )
    }

    fun setupmttq(){

        try {
            val token = client().connect()
            token.actionCallback = object : IMqttActionListener {
                override fun onSuccess(asyncActionToken: IMqttToken) {
                    // We are connected
                    toast("connected")
                    if (client().isConnected){
                        publish(client())
                        subscribe(client())
                    }else{
                        toast("no client connected")
                    }

                }

                override fun onFailure(asyncActionToken: IMqttToken, exception: Throwable) {
                    // Something went wrong e.g. connection timeout or firewall problems
                    toast("failure couldn't connect")

                }
            }
        } catch (e: MqttException) {
            e.printStackTrace()
        }

    }

    fun publish(client: MqttAndroidClient){
        val topic = "foo/bar"
        val payload = "the payload"
        var encodedPayload: ByteArray
        try {
            encodedPayload = payload.toByteArray(charset("UTF-8"))
            val message = MqttMessage (encodedPayload)
            client.publish(topic, message)
            toast("message published")
        } catch (e: UnsupportedEncodingException) {
            e.printStackTrace()
        } catch (e: MqttException) {
            e.printStackTrace()
        }

    }

    fun subscribe(client: MqttAndroidClient){
        val topic = "foo/bar"
        val qos = 1
        try {
            val subToken = client.subscribe(topic, qos)
            subToken.actionCallback = object : IMqttActionListener {
                override fun onSuccess(asyncActionToken: IMqttToken) {
                    // The message was published
                    toast(asyncActionToken.topics[0])

                }

                override fun onFailure(
                    asyncActionToken: IMqttToken,
                    exception: Throwable
                ) {
                    toast("you are not subscribed to the publisher")
                    // The subscription could not be performed, maybe the user was not
                    // authorized to subscribe on the specified topic e.g. using wildcards

                }
            }
        } catch (e: MqttException) {
            e.printStackTrace()
        }
        client.setCallback(object :MqttCallback{
            override fun messageArrived(topic: String?, message: MqttMessage?) {
                toast(message.toString())
                logs("===========messaged received $message")
            }

            override fun connectionLost(cause: Throwable?) {
                toast("==========connection lost")
                logs("connection was lost $cause")
            }

            override fun deliveryComplete(token: IMqttDeliveryToken?) {
                toast("==========message delivered")
                logs("messages has be deleivered")
            }

        })

    }




}
