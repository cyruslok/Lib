import math
import File 

class GeoMath:

    EarthRadius = 6371000#Meters
    FileSave = File.Write()

    def __init__(self):
        pass

    def MeterToEarthDeg(self, length_meters):
        l = length_meters
        deg = (l/(math.pi * self.EarthRadius))*180
        return deg

    def DegToMeter(self, deg):
        m = ((deg)/180)*(math.pi*self.EarthRadius)
        return m

    def getLatLonGridPoints(self, start_lat_lon, end_lat_lon, length_meters):
        points = []
        deg_change = self.MeterToEarthDeg(length_meters)

        start_lat = start_lat_lon['lat']; #print(start_lat)
        start_lon = start_lat_lon['lon']; #print(start_lon)
        end_lat = end_lat_lon['lat']; #print(end_lat)
        end_lon = end_lat_lon['lon']; #print(end_lon)
        current_lat = end_lat
        current_lon = end_lon

        while current_lat<=start_lat-deg_change:
            current_lat+=deg_change
            while current_lon>=start_lon+deg_change:
                current_lon-=deg_change
                points.append({'lat': current_lat, 'lon':current_lon})
                points.append({'lat': current_lat-(deg_change/2), 'lon':current_lon-(deg_change/2)})
            current_lon=end_lon
        self.FileSave.SaveJsonObject('../asset' , 'temp.json', points)
        return points

    def getMeterSizeBy2Coordinate(self, start_lat, start_lon, end_lat, end_lon):
        widthdeg = ( (end_lon**2)**0.5 )  - ( (start_lon**2)**0.5 )
        heightdeg = ( (end_lat**2)**0.5 )  - ( (start_lat**2)**0.5 ) 
        widthdeg = (widthdeg**2)**0.5
        heightdeg = (heightdeg**2)**0.5
        widthMeter = self.DegToMeter(widthdeg)
        heightMeter = self.DegToMeter(heightdeg)
        return [widthMeter, heightMeter]


'''
        lat_n = (((start_lat-end_lat)**2)**0.5)/deg_change
        for i in range(1, int(lat_n)):
            current_lat += deg_change
            points.append({'lat':current_lat, 'lon':current_lon})

        self.FileSave.SaveJsonObject('../asset' , 'temp.json', points)
        return points'''