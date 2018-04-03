package br.com.farmonline.web;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import se.walkercrou.places.*;
import br.com.farmonline.mapa.*;

@ManagedBean(name = "buscaMapa", eager = true)
public class BuscaMapaBean implements Serializable {

	private static final long serialVersionUID = 1L;

	@PostConstruct

	public void init() {

	}

	private String address;

	private static List<MapLocationPharma> InfoMaps;

	private static final ArrayList<MapLocationPharma> farmalist = new ArrayList<MapLocationPharma>();

	public void SearchGeolocation() {

		final String PLACES_API_BASE = "https://maps.googleapis.com/maps/api/geocode";
		final String OUT_JSON = "/json";
		final String API_KEY = "AIzaSyD1O5CPrb7NfaS9KJUnsZb1-dwwieeM5Do";
		// final String address = "04473000";
		ArrayList<Place> resultList = null;
		HttpURLConnection conn = null;
		StringBuilder jsonResults = new StringBuilder();
		try {
			StringBuilder sb = new StringBuilder(PLACES_API_BASE);

			sb.append(OUT_JSON);
			sb.append("?sensor=false");
			sb.append("&address=" + URLEncoder.encode(address, "utf8"));
			sb.append("&key=" + API_KEY);

			URL url = new URL(sb.toString());
			conn = (HttpURLConnection) url.openConnection();
			InputStreamReader in = new InputStreamReader(conn.getInputStream());
			int read;
			char[] buff = new char[1024];
			while ((read = in.read(buff)) != -1) {
				jsonResults.append(buff, 0, read);
			}
		} catch (Exception e) {

			System.out.println("Error processing Places API URL");

		} finally {
			if (conn != null) {
				conn.disconnect();
			}
		}
		try {

			JSONObject jsonObj = new JSONObject(jsonResults.toString());
			JSONArray predsJsonArray = jsonObj.getJSONArray("results");

			resultList = new ArrayList<Place>(predsJsonArray.length());

			Double lat = 0.0, lon = 0.0;
			for (int i = 0; i < predsJsonArray.length(); i++) {

				JSONObject json = new JSONObject();

				JSONObject geoMetryObject = new JSONObject();
				JSONObject locations = new JSONObject();
				json = predsJsonArray.getJSONObject(i);
				geoMetryObject = json.getJSONObject("geometry");
				locations = geoMetryObject.getJSONObject("location");
				lat = locations.getDouble("lat");
				lon = locations.getDouble("lng");
			}
			SearchTextPlace(lon, lat);
		} catch (JSONException e) {
			System.out.println("Error processing JSON results");
		}
	}

	public List<MapLocationPharma> SearchTextPlace(double Longitude, double Latidude) {
		final String PLACES_API_BASE = "https://maps.googleapis.com/maps/api/place";
		// https://maps.googleapis.com/maps/api/geocode/json?address=04473000&key=AIzaSyD9PYSEEdh_uCZ-5UmRvPzvCYMItbRLMP8
		final String TYPE_SEARCH = "/textsearch";
		final String OUT_JSON = "/json"; // KEY!
		final String API_KEY = "AIzaSyD1O5CPrb7NfaS9KJUnsZb1-dwwieeM5Do";
		final String keyword = "farmacia";
		final int radius = 500;

		ArrayList<Place> resultList = null;
		HttpURLConnection conn = null;
		StringBuilder jsonResults = new StringBuilder();
		try {
			StringBuilder sb = new StringBuilder(PLACES_API_BASE);
			sb.append(TYPE_SEARCH);
			sb.append(OUT_JSON);
			sb.append("?sensor=false");
			sb.append("&key=" + API_KEY);
			sb.append("&query=" + URLEncoder.encode(keyword, "utf8"));
			sb.append("&location=" + String.valueOf(Latidude) + "," + String.valueOf(Longitude));
			sb.append("&radius=" + String.valueOf(radius));
			URL url = new URL(sb.toString());
			conn = (HttpURLConnection) url.openConnection();
			InputStreamReader in = new InputStreamReader(conn.getInputStream(), "UTF-8");
			int read;
			char[] buff = new char[1024];
			while ((read = in.read(buff)) != -1) {
				jsonResults.append(buff, 0, read);
			}
		} catch (Exception e) {

			System.out.println("Error processing Places API URL");

		} finally {
			if (conn != null) {
				conn.disconnect();
			}
		}

		try {

			// Create a JSON object hierarchy from the results
			JSONObject jsonObj = new JSONObject(jsonResults.toString());
			JSONArray predsJsonArray = jsonObj.getJSONArray("results");

			// Extract the Place descriptions from the results
			resultList = new ArrayList<Place>(predsJsonArray.length());
			for (int i = 0; i < predsJsonArray.length(); i++) {

				// place.reference = predsJsonArray.getJSONObject(i).getString("reference");

				String photoReference = null;

				InfoMaps = new ArrayList<MapLocationPharma>();

				MapLocationPharma mapa = new MapLocationPharma();

				mapa.setNomeEstabelecimento(predsJsonArray.getJSONObject(i).getString("name"));

				mapa.setEndereco(predsJsonArray.getJSONObject(i).getString("formatted_address"));

				// @Paulo Graciano - coments
				// Photo Reference recovery of google, but did no work because need only get url
				// for bind in img src

				/*
				 * JSONObject jsonPhoto= new JSONObject(); jsonPhoto =
				 * predsJsonArray.getJSONObject(i);
				 * 
				 * JSONArray photosnArray = jsonPhoto.getJSONArray("photos");
				 * 
				 * for (int p = 0; p < photosnArray.length(); p++) {
				 * 
				 * photoReference = photosnArray.getJSONObject(i).getString("photo_reference");
				 * 
				 * }
				 * 
				 * mapa.setUrlImage(SearchPhotoReference(photoReference));
				 * 
				 * 
				 * 
				 * /*geoPhotoObject = predsJsonArray.getJSONObject(i)..getString("photos");
				 * JSONObject photo = new JSONObject(geoPhotoObject.toString());
				 * 
				 * photo = geoPhotoObject.getJSONObject("photos"); photoDetails =
				 * photo.getJSONObject("photo_reference");
				 */

				farmalist.add(mapa);
			}
		}

		catch (JSONException e) {
			System.out.println("Error processing JSON results");

		}
		return InfoMaps;

	}

	public String SearchPhotoReference(String photoReference) {

		final String PLACES_API_BASE = "https://maps.googleapis.com/maps/api/place/photo";
		final String MAXWIDTH = "400";
		final String API_KEY = "AIzaSyD1O5CPrb7NfaS9KJUnsZb1-dwwieeM5Do";
		String charset = StandardCharsets.UTF_8.name();
		// final String address = "04473000";
		ArrayList<Place> resultList = null;
		HttpURLConnection conn = null;
		StringBuilder jsonResults = new StringBuilder();
		try {

			StringBuilder sb = new StringBuilder(PLACES_API_BASE);

			sb.append("?maxwidth=" + MAXWIDTH);
			sb.append("&photoreference=" + URLEncoder.encode(photoReference, "utf8"));
			sb.append("&key=" + API_KEY);

			URL url = new URL(sb.toString());
			conn = (HttpURLConnection) url.openConnection();
			// String teste = conn.getURL().getPath();
			// InputStreamReader in = new InputStreamReader(conn.getInputStream());

			// URL url = new URL("http://www.objects.com.au/services/sherpa.html");
			BufferedInputStream in = new BufferedInputStream(url.openStream());
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			byte[] buf = new byte[1024];
			int n = 0;
			while (-1 != (n = in.read(buf))) {
				out.write(buf, 0, n);
			}
			out.close();
			in.close();
			byte[] response = out.toByteArray();

		} catch (Exception e) {

			System.out.println("Error processing Places API URL");

		} finally {
			if (conn != null) {
				conn.disconnect();
			}
		}
		try {

			JSONObject jsonObj = new JSONObject(jsonResults.toString());
			JSONArray predsJsonArray = jsonObj.getJSONArray("results");

			resultList = new ArrayList<Place>(predsJsonArray.length());

			Double lat = 0.0, lon = 0.0;
			for (int i = 0; i < predsJsonArray.length(); i++) {

				JSONObject json = new JSONObject();

				JSONObject geoMetryObject = new JSONObject();
				JSONObject locations = new JSONObject();
				json = predsJsonArray.getJSONObject(i);
				geoMetryObject = json.getJSONObject("geometry");
				locations = geoMetryObject.getJSONObject("location");
				lat = locations.getDouble("lat");
				lon = locations.getDouble("lng");
			}
			SearchTextPlace(lon, lat);
		} catch (JSONException e) {
			System.out.println("Error processing JSON results");
		}

		return photoReference;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public static List<MapLocationPharma> getInfoMaps() {
		return InfoMaps;
	}

	public ArrayList<MapLocationPharma> getfarmalist() {

		return farmalist;

	}
}
