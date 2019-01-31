/*******************************************************************************
 * Copyright (C) 2019 ICCU - Istituto Centrale per il Catalogo Unico
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published
 * by the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 * 
 * You should have received a copy of the GNU Affero General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package BibliotecheImporter.BibliotecheImporter;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.maps.GeoApiContext;
import com.google.maps.GeocodingApi;
import com.google.maps.model.GeocodingResult;

import utils.Constants;

public class GoogleMapsTestGeocode {

	public static void main(String[] args) throws Exception {
		GeoApiContext context = new GeoApiContext().setApiKey(Constants.maps_api_key);
			
		GeocodingResult[] results =  GeocodingApi.geocode(context,
			    "Via schilpario 9, Roma").await();
			Gson gson = new GsonBuilder().setPrettyPrinting().create();
			System.out.println(gson.toJson(results));

	}

}
