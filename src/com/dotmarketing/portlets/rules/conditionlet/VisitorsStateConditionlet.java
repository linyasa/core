package com.dotmarketing.portlets.rules.conditionlet;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dotcms.repackage.com.maxmind.geoip2.exception.GeoIp2Exception;
import com.dotcms.util.GeoIp2CityDbUtil;
import com.dotcms.util.HttpRequestDataUtil;
import com.dotmarketing.portlets.rules.model.ConditionValue;
import com.dotmarketing.util.Logger;
import com.dotmarketing.util.UtilMethods;

/**
 * This conditionlet will allow CMS users to check the state/province/region a
 * user request comes from. The available options of this conditionlet will be
 * represented as a one or two-character values.
 * <p>
 * The location of the request is determined by the IP address of the client
 * that issued the request. Geographic information is then retrieved via the <a
 * href="http://maxmind.github.io/GeoIP2-java/index.html">GeoIP2 Java API</a>,
 * which will allow CMS users to display content based on geographic data.
 * </p>
 * 
 * @author Jose Castro
 * @version 1.0
 * @since 04-13-2015
 *
 */
public class VisitorsStateConditionlet extends Conditionlet {

	private static final long serialVersionUID = 1L;

	private static final String INPUT_ID = "state";
	private static final String CONDITIONLET_NAME = "User's State/Province/Region";
	private static final String COMPARISON_IS = "is";
	private static final String COMPARISON_ISNOT = "isNot";

	private LinkedHashSet<Comparison> comparisons = null;
	private Map<String, ConditionletInput> inputValues = null;

	@Override
	protected String getName() {
		return CONDITIONLET_NAME;
	}

	@Override
	public Set<Comparison> getComparisons() {
		if (this.comparisons == null) {
			this.comparisons = new LinkedHashSet<Comparison>();
			this.comparisons.add(new Comparison(COMPARISON_IS, "Is"));
			this.comparisons.add(new Comparison(COMPARISON_ISNOT, "Is Not"));
		}
		return this.comparisons;
	}

	@Override
	public ValidationResults validate(Comparison comparison,
			Set<ConditionletInputValue> inputValues) {
		ValidationResults results = new ValidationResults();
		if (UtilMethods.isSet(inputValues)) {
			List<ValidationResult> resultList = new ArrayList<ValidationResult>();
			// Validate all available input fields
			for (ConditionletInputValue inputValue : inputValues) {
				ValidationResult validation = validate(comparison, inputValue);
				if (!validation.isValid()) {
					resultList.add(validation);
					results.setErrors(true);
				}
			}
			results.setResults(resultList);
		}
		return results;
	}

	@Override
	protected ValidationResult validate(Comparison comparison,
			ConditionletInputValue inputValue) {
		ValidationResult validationResult = new ValidationResult();
		String inputId = inputValue.getConditionletInputId();
		if (UtilMethods.isSet(inputId)) {
			String selectedValue = inputValue.getValue();
			ConditionletInput inputField = this.inputValues.get(inputId);
			validationResult.setConditionletInputId(inputId);
			Set<EntryOption> inputOptions = inputField.getData();
			for (EntryOption option : inputOptions) {
				// Validate that the selected value is correct
				if (option.getId().equals(selectedValue)) {
					validationResult.setValid(true);
					break;
				}
			}
			if (!validationResult.isValid()) {
				validationResult.setErrorMessage("Invalid value for input '"
						+ inputField.getId() + "': '" + selectedValue + "'");
			}
		}
		return validationResult;
	}

	@Override
	public Collection<ConditionletInput> getInputs(String comparisonId) {
		if (this.inputValues == null) {
			ConditionletInput inputField = new ConditionletInput();
			// Set field configuration and available options
			inputField.setId(INPUT_ID);
			inputField.setMultipleSelectionAllowed(true);
			inputField.setDefaultValue("");
			inputField.setMinNum(1);
			Set<EntryOption> options = new LinkedHashSet<EntryOption>();
			options.add(new EntryOption("AK", "Alaska"));
			options.add(new EntryOption("AL", "Alabama"));
			options.add(new EntryOption("AR", "Arkansas"));
			options.add(new EntryOption("AZ", "Arizona"));
			options.add(new EntryOption("CA", "California"));
			options.add(new EntryOption("CO", "Colorado"));
			options.add(new EntryOption("CT", "Connecticut"));
			options.add(new EntryOption("DE", "Delaware"));
			options.add(new EntryOption("FL", "Florida"));
			options.add(new EntryOption("GA", "Georgia"));
			options.add(new EntryOption("HI", "Hawaii"));
			options.add(new EntryOption("IA", "Iowa"));
			options.add(new EntryOption("ID", "Idaho"));
			options.add(new EntryOption("IL", "Illinois"));
			options.add(new EntryOption("IN", "Indiana"));
			options.add(new EntryOption("KS", "Kansas"));
			options.add(new EntryOption("KY", "Kentucky"));
			options.add(new EntryOption("LA", "Louisiana"));
			options.add(new EntryOption("MA", "Massachusetts"));
			options.add(new EntryOption("MD", "Maryland"));
			options.add(new EntryOption("ME", "Maine"));
			options.add(new EntryOption("MI", "Michingan"));
			options.add(new EntryOption("MN", "Minnesota"));
			options.add(new EntryOption("MO", "Missouri"));
			options.add(new EntryOption("MS", "Mississippi"));
			options.add(new EntryOption("MT", "Montana"));
			options.add(new EntryOption("NC", "North Carolina"));
			options.add(new EntryOption("ND", "North Dakota"));
			options.add(new EntryOption("NE", "Nebraska"));
			options.add(new EntryOption("NH", "New Hampshire"));
			options.add(new EntryOption("NJ", "New Jersey"));
			options.add(new EntryOption("NM", "New Mexico"));
			options.add(new EntryOption("AK", "Alaska"));
			options.add(new EntryOption("NV", "Nevada"));
			options.add(new EntryOption("NY", "New York"));
			options.add(new EntryOption("OH", "Ohio"));
			options.add(new EntryOption("OK", "Oklahoma"));
			options.add(new EntryOption("OR", "Oregon"));
			options.add(new EntryOption("PA", "Pennsylvania"));
			options.add(new EntryOption("RI", "Rhode Island"));
			options.add(new EntryOption("SC", "South Carolina"));
			options.add(new EntryOption("SD", "South Dakota"));
			options.add(new EntryOption("TN", "Tennessee"));
			options.add(new EntryOption("TX", "Texas"));
			options.add(new EntryOption("UT", "Utah"));
			options.add(new EntryOption("VA", "Virginia"));
			options.add(new EntryOption("VT", "Vermont"));
			options.add(new EntryOption("WA", "Washington"));
			options.add(new EntryOption("WI", "Wisconsin"));
			options.add(new EntryOption("WV", "West Virginia"));
			options.add(new EntryOption("WY", "Wyoming"));
			inputField.setData(options);
			this.inputValues = new LinkedHashMap<String, ConditionletInput>();
			this.inputValues.put(inputField.getId(), inputField);
		}
		return this.inputValues.values();
	}

	@Override
	public boolean evaluate(HttpServletRequest request,
			HttpServletResponse response, String comparisonId,
			List<ConditionValue> values) {
		boolean result = false;
		if (UtilMethods.isSet(comparisonId) && UtilMethods.isSet(values)
				&& UtilMethods.isSet(comparisonId)) {
			GeoIp2CityDbUtil geoIp2Util = GeoIp2CityDbUtil.getInstance();
			String ipAddress = null;
			String state = null;
			try {
				InetAddress address = HttpRequestDataUtil.getIpAddress(request);
				ipAddress = address.getHostAddress();
				state = geoIp2Util.getSubdivisionIsoCode(ipAddress);
			} catch (UnknownHostException e) {
				Logger.error(this,
						"Could not retrieved a valid IP address from request: "
								+ request.getRequestURL());
			} catch (IOException e) {
				Logger.error(this,
						"Could not establish connection to GeoIP2 database for IP "
								+ ipAddress);
			} catch (GeoIp2Exception e) {
				Logger.error(this,
						"State/province/region code could not be retreived for IP "
								+ ipAddress);
			}
			if (UtilMethods.isSet(state)) {
				Comparison comparison = getComparisonById(comparisonId);
				Set<ConditionletInputValue> inputValues = new LinkedHashSet<ConditionletInputValue>();
				for (ConditionValue value : values) {
					inputValues.add(new ConditionletInputValue(INPUT_ID, value
							.getValue()));
				}
				ValidationResults validationResults = validate(comparison,
						inputValues);
				if (!validationResults.hasErrors()) {
					// If state is equal to one or more options...
					if (comparison.getId().equals(COMPARISON_IS)) {
						for (ConditionValue value : values) {
							if (value.getValue().equals(state)) {
								result = true;
								break;
							}
						}
						// If state is distinct from the selected options...
					} else if (comparison.getId().equals(COMPARISON_ISNOT)) {
						result = true;
						for (ConditionValue value : values) {
							if (value.getValue().equals(state)) {
								result = false;
								break;
							}
						}
					}
				}
			}
		}
		return result;
	}

}
