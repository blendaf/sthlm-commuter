# Travel Mode Function

### TravelMode
Creates a travelMode object that consists of a string that describes the different travel modes. 

### TravelModeQuery
Creates a TravelModeQuery object that consist of a list with travelModes and adds modes to the list. 

### fromStringList
Takes a string containing several travel modes, parses the string and creates a new TravelModeQuery object(list of TravelMode objects).  

### TransportMode
Contains strings corresponding to the different travel modes(probably according to the APIs own naming conventions). Also contains index variables for each of the travel modes, the purpose of which is still unknown. A *getIndex* function also exists which returns the index of the corresponding travel mode. 

### uriV2
Decodes a URI and calls the *fromStringList* function to create a TravelModeQuery object. Creates a list of TransportMode objects by translating the TravelModeQuery object (a list of TravelMode objects).

### toUri

The function *toUri* converts a list with transportModes objects to a list with travelMode objects through the *transportModesToTravelModes* function. A new travelModeQuery object is created based on the converted list of travelMode objects. 

The travel modes are added to a URI in the form *transportModes="travelmode"*

### LegUtil.java
The functions *transportModesToTravelModes* and *travelModesToTransportModes* converts a list of transportMode objects to a list of travelMode objects and vice versa.  

The *getTransportDrawable* function looks at the travelmode of the leg (led.getTravelMode) and supplies an icon accordingly. 

### JourneyQuery
From a parcel object a JourneyQuery object is created. The object consist of, amongst other things, a ArrayList of strings called *transportModes*. *transportModes* consists of user decided travel modes accessed from backend.
**This is where user input is accessed**

### hasAdditionalFiltering
Checks if the user have filtered travel modes. 

### toJson 
`
public JSONObject toJson(boolean all) throws JSONException
`
 


### onSearchRoutes
The function calls the toUri function with parameter *withTime* parameter set to true. 

```
journeyQuery.toUri(true);
```
The function creates a new *android.content.Intent* object called *routesIntent*. The activity is RoutesActivity.EXTRA_JOURNEY_QUERY and is based upon the URI. Then the function calls  the *startActivity* function in the Fragment.java file with the *routesIntent* variable as a parameter. 

### nCreateShortCut
The function saves the URI in a variable called *routesUri*. A new *android.content.Intent* object is then created based upon *routesUri*. An *android.content.Intent* object is an abstract description of an operation to be performed. It can be used with startActivity to launch an Activity. In this specific case the activity is to view the information specified by the URI. 

### Leg.java
In the function *Leg*, travel modes decided by the user is assigned to the variable *travelMode*.


### planTransit
```
public void planTransit(final JourneyQuery journeyQuery, final Callback callback, final String ident, final @Nullable String dir)
```
A list of TravelMode object is created from the JourneyQuery object's transportModes arraylist. A new TravelModeQuery object is created from the list. The *apiService.getPlan* function is called with the TravelModeQuery as a parameter. The travel mode setting are sent to the backend and then forwarded to the SL API. 

 


## File Structure

* models/TravelMode.java
* api/TravelModeQuery.java
* planner/JourneyQuery.java
* api/ApiService.java
* models/Leg.java
* models/Route.java (?)
* routing/Router.java 
* site/PlacesProvider.java
* site/TransportMode.java
* service/DataMigrationService.java
* utils/LegUtil.java
* utils/ViewHelper.java
* utils/ChangeRouteTimeActivity.java
* utils/DeparturesAdepter.java
* utils/DeparturesActivity.java
* utils/FavoritesFragent.java
* utils/PlannerFragment.java
* utils/UriLauncherActivity.java


