using System;
using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.UI;

public class LogScrollRect : MonoBehaviour {

	public GameObject LogText;
	public Transform Contect;
	public Button ButtonClear;
	bool isClearing = false;

	void Start(){
		ButtonClear.onClick.AddListener (() => {
			try{
				for(int i = 0 ; i < Contect.childCount ; i ++){
					Destroy( Contect.GetChild(i).gameObject );
					isClearing = true;
				}
			}catch(Exception e){
				Debug.Log(e.Message);
			}
			isClearing = false;
		});
	}
	public void Log(string msg, Color color = default( Color )){
		if (!isClearing) {
			GameObject go = Instantiate (LogText, Vector3.zero, Quaternion.identity, Contect.transform) as GameObject;
			Text log_text = go.GetComponent<Text> ();
			log_text.text = msg;
			log_text.color = color;
			this.GetComponent<ScrollRect>().verticalNormalizedPosition = 0;
		}
	}
}
