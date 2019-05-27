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
package it.almaviva.utils.opac;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.List;
import java.util.ListIterator;

import it.almaviva.opac.bean.ricerca.result.MatchType;
import it.almaviva.opac.bean.ricerca.result.OperatorType;
import it.almaviva.opac.bean.ricerca.ricerca.DateNovitaType;
import it.almaviva.opac.bean.ricerca.ricerca.Filter;
import it.almaviva.opac.bean.ricerca.ricerca.Filters;
import it.almaviva.opac.bean.ricerca.ricerca.GroupFilters;
import it.almaviva.opac.services.ConverterCampiServices;

public class Util {
	private static ConverterCampiServices converter = new ConverterCampiServices();
	private static SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
	public static <T> List<T> listOf(T value) {
		List<T> list = new ArrayList<>();
		list.add(value);
		return list;
	}

	public static boolean isFilled(Object value) {
		return (value != null);
	}

	public static boolean isFilled(String value) {
		return (value != null && !"".equals(value.trim()));
	}

	public static final boolean isFilled(Collection<?> value) {
		return (value != null && value.size() > 0);
	}

	public static final boolean isFilled(Object[] value) {
		return (value != null && !(value.length < 1));
	}

	public static List<String> readFile(InputStream in) {
		List<String> output = new ArrayList<>();
		try (BufferedReader br = new BufferedReader(new InputStreamReader(in))) {
			String line;
			while ((line = br.readLine()) != null) {
				output.add(line);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return output;
	}

	public static int len(Object[] value) {
		if (value != null)
			return value.length;

		return 0;
	}

	public static int len(char[] value) {
		if (value != null)
			return value.length;

		return 0;
	}
	public static String cleanPath(String path) {
		path = path.replace("\\", File.separator);
		path = path.replace("/", File.separator);
		return path;
	}
	public static boolean isNumeric(String input) {
		try {
			Integer.parseInt(input);
			return true;
		} catch (NumberFormatException e) {
			// s is not numeric
			return false;
		}
	}

	public static String normalizeStort(String sort) {
		if (sort.equals("titolo_sint"))
			sort = "syntetic_title";
		return sort;
	}

	public static GroupFilters andwordFilters(GroupFilters filtersGroup) {
		GroupFilters appoggio = new GroupFilters();
		List<Filters> gruppiapp = new ArrayList<Filters>();

		List<Filters> filters = filtersGroup.getFilters();
		for (Filters ftrs : filters) {
			Filters appoggioGroups = new Filters();

			List<Filter> filtriAppoggio = new ArrayList<Filter>();
			List<Filter> filtri = ftrs.getFilters();
			for (int i = 0; i < filtri.size(); i++) {
				Filter filtro = filtri.get(i);
				if (filtro.getMatch().equals(MatchType.andWord) && !filtro.getField().toLowerCase().equals("dewey_code")) {
					List<String> list = new ArrayList<String>();

					filtro.setField(converter.converToPublic(filtro.getField()));
					filtro.setValue(filtro.getValue().replaceAll("[\\p{Punct} && [^*]]+", " "));
					filtro.setValue(filtro.getValue().replaceAll("\\s+", " "));
					filtro.setValue(filtro.getValue().trim());
					list = Arrays.asList(filtro.getValue().split(" "));
					ListIterator<String> paroleListITerator = list.listIterator();
					for (String parola : list) {
						paroleListITerator.next();
						Filter appoggioFiltro = new Filter();
						appoggioFiltro.setField(filtro.getField());
						if (paroleListITerator.hasNext())
							appoggioFiltro.setOperator(OperatorType.AND);
						else
							appoggioFiltro.setOperator(filtro.getOperator());
						appoggioFiltro.setMatch(filtro.getMatch());
						appoggioFiltro.setValue(parola);
						appoggioFiltro.setOtherFiltersGroup(
								tranformSingleFiltersGroupInFiltersGroup(filtro.getOtherFiltersGroup()));
						filtriAppoggio.add(appoggioFiltro);
					}
				} else {
					// filtro.setField(converter.converToPublic(filtro.getField()));
					filtriAppoggio.add(filtro);
				}
				appoggioGroups.setFilters(filtriAppoggio);
				// appoggioGroups.setOperator(filters.get(i).getOperator());
				try {
					appoggioGroups.setOperator(filters.get(i).getOperator());
				} catch (Exception e) {
					appoggioGroups.setOperator(OperatorType.AND);
				}

			}
			gruppiapp.add(appoggioGroups);

		}
		appoggio.setFilters(gruppiapp);

		return appoggio;
	}

	private static List<Filter> tranformSingleFiltersGroupInFiltersGroup(List<Filter> toTransform) {
		if (toTransform == null)
			return null;

		List<Filter> transformeds = new ArrayList<Filter>();
		toTransform.forEach(filtroToTransform -> {
			String[] valueParts = filtroToTransform.getValue().split(" ");
			if (valueParts.length > 1 && filtroToTransform.getMatch().equals(MatchType.andWord))
				// Foreach parole
				for (String valuePart : valueParts) {
					Filter newFiltro = new Filter(filtroToTransform.getField(), valuePart,
							filtroToTransform.getOperator(), filtroToTransform.getMatch());
					transformeds.add(newFiltro);
				}
			else
				transformeds.add(filtroToTransform);
		});
		return ((transformeds.size() == 0) ? null : transformeds);
	}

	public static String checkNovitaDate(String date) {
		date = date.trim().toUpperCase();
		Calendar cal = Calendar.getInstance();
		switch (DateNovitaType.checkDate(date)) {
		case IERI: {
			cal.add(Calendar.DATE, -1);
			return sdf.format(cal.getTime());
		}
		case OGGI: {
			return sdf.format(cal.getTime());
		}
		case LAST_WEEK: {
			cal.add(Calendar.DATE, -7);
			return sdf.format(cal.getTime());
		}
		case LAST_MONTH: {
			cal.add(Calendar.MONTH, -1);
			return sdf.format(cal.getTime());
		}


		default:
			return date;
		}
		
	}
}
