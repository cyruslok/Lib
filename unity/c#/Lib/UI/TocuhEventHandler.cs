using System;
using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.UI;
using UnityEngine.EventSystems;

public class TocuhEventHandler : MonoBehaviour , IPointerDownHandler , IPointerUpHandler {

	private float onObjectTime = 0;
	private bool isDetect;

	public float MaxClickTime = 0.5f;
	public float DragRange = 10f;

	private Vector3 ScreenPointStart;
	private Vector3 ScreenPointCurrent;
	private Vector3 ScreePointDistance;

	// Handler
	public Action OnClickEventHandler = null;
	public Action OnLongPressEventHandler = null;
	public Action<Vector2> OnDragEventHandler = null; 

	void Update(){
		if (isDetect) {
			onObjectTime += Time.deltaTime;
			ScreenPointCurrent = Input.mousePosition;
			ScreePointDistance = ScreenPointStart - ScreenPointCurrent;
			if (Vector3.Distance (ScreenPointStart, ScreenPointCurrent) > DragRange) {
				Vector2 DistancePoint = new Vector2 (ScreePointDistance.x, ScreePointDistance.y);
				if(OnDragEventHandler!=null) { OnDragEventHandler (DistancePoint); }
				Debug.Log("On Drag >> " + DistancePoint.ToString());
				isDetect = false;
			}

			if (onObjectTime>MaxClickTime) {
				if (OnLongPressEventHandler != null) { OnLongPressEventHandler (); }
				Debug.Log("Long Press");
				isDetect = false;
			}
		}
	}


	public void OnPointerUp(PointerEventData eventData){
		if (onObjectTime < MaxClickTime && isDetect == true) {
			//OnClick
			if (OnClickEventHandler != null) { OnClickEventHandler (); }
			Debug.Log("On Click");
		}
		isDetect = false;
		onObjectTime = 0;
		ScreenPointStart = Vector3.zero;
	}
	public void OnPointerDown(PointerEventData eventData)
	{
		ScreenPointStart = Input.mousePosition;
		ScreenPointCurrent = ScreenPointStart;
		isDetect = true;
	}
}
