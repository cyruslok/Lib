using System.Collections;
using System.Collections.Generic;
using System.IO;
using System.Text;
using System;
using UnityEngine;
using UnityEngine.UI;

namespace Unity.Tools.Tex2D{
	public class Texture2DTools {
		public Texture2D RotateSquare(Texture2D texture, float eulerAngles){
			int x;
			int y;
			int i;
			int j;
			float phi = eulerAngles/(180/Mathf.PI);
			float sn = Mathf.Sin(phi);
			float cs = Mathf.Cos(phi);
			Color32[] arr = texture.GetPixels32();
			Color32[] arr2 = new Color32[arr.Length];
			int W = texture.width;
			int H = texture.height;
			int xc = W/2;
			int yc = H/2;

			for (j=0; j<H; j++){
				for (i=0; i<W; i++){
					arr2[j*W+i] = new Color32(0,0,0,0);

					x = (int)(cs*(i-xc)+sn*(j-yc)+xc);
					y = (int)(-sn*(i-xc)+cs*(j-yc)+yc);

					if ((x>-1) && (x<W) &&(y>-1) && (y<H)){ 
						arr2[j*W+i]=arr[y*W+x];
					}
				}
			}

			Texture2D newImg = new Texture2D (W, H);
			newImg.SetPixels32 (arr2);
			newImg.Apply();

			return newImg;
		}

		public Texture2D ResampleAndCrop(Texture2D source, int targetWidth, int targetHeight)
		{
			int sourceWidth = source.width;
			int sourceHeight = source.height;
			float sourceAspect = (float)sourceWidth / sourceHeight;
			float targetAspect = (float)targetWidth / targetHeight;
			int xOffset = 0;
			int yOffset = 0;
			float factor = 1;
			if (sourceAspect > targetAspect)
			{ // crop width
				factor = (float)targetHeight / sourceHeight;
				xOffset = (int)((sourceWidth - sourceHeight * targetAspect) * 0.5f);
			}
			else
			{ // crop height
				factor = (float)targetWidth / sourceWidth;
				yOffset = (int)((sourceHeight - sourceWidth / targetAspect) * 0.5f);
			}
			Color32[] data = source.GetPixels32();
			Color32[] data2 = new Color32[targetWidth * targetHeight];
			for (int y = 0; y < targetHeight; y++)
			{
				for (int x = 0; x < targetWidth; x++)
				{
					var p = new Vector2(Mathf.Clamp(xOffset + x / factor, 0, sourceWidth - 1), Mathf.Clamp(yOffset + y / factor, 0, sourceHeight - 1));
					// bilinear filtering
					var c11 = data[Mathf.FloorToInt(p.x) + sourceWidth * (Mathf.FloorToInt(p.y))];
					var c12 = data[Mathf.FloorToInt(p.x) + sourceWidth * (Mathf.CeilToInt(p.y))];
					var c21 = data[Mathf.CeilToInt(p.x) + sourceWidth * (Mathf.FloorToInt(p.y))];
					var c22 = data[Mathf.CeilToInt(p.x) + sourceWidth * (Mathf.CeilToInt(p.y))];
					var f = new Vector2(Mathf.Repeat(p.x, 1f), Mathf.Repeat(p.y, 1f));
					data2[x + y * targetWidth] = Color.Lerp(Color.Lerp(c11, c12, p.y), Color.Lerp(c21, c22, p.y), p.x);
				}
			}

			var tex = new Texture2D(targetWidth, targetHeight);
			tex.SetPixels32(data2);
			tex.Apply(true);
			return tex;
		}

		public Texture2D ScaleTexture (Texture2D source, int targetWidth, int targetHeight)
		{
			Texture2D result = new Texture2D (targetWidth, targetHeight, source.format, true);
			Color[] rpixels = result.GetPixels (0);
			float incX = ((float)1 / source.width) * ((float)source.width / targetWidth);
			float incY = ((float)1 / source.height) * ((float)source.height / targetHeight);
			for (int px = 0; px < rpixels.Length; px++) {
				rpixels [px] = source.GetPixelBilinear (incX * ((float)px % targetWidth), incY * ((float)Mathf.Floor (px / targetWidth)));
			}
			result.SetPixels (rpixels, 0);
			result.Apply ();
			return result;
		}

	}
}