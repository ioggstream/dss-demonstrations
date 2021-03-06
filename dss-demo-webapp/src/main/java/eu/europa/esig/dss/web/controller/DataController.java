package eu.europa.esig.dss.web.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import eu.europa.esig.dss.enumerations.SignatureForm;
import eu.europa.esig.dss.enumerations.SignatureLevel;
import eu.europa.esig.dss.enumerations.SignaturePackaging;
import eu.europa.esig.dss.utils.Utils;

@Controller
@RequestMapping(value = "/data")
public class DataController {
	
	private static final String[] ALLOWED_FIELDS = { "form", "isSign" };
	
	@InitBinder
	public void setAllowedFields(WebDataBinder webDataBinder) {
		webDataBinder.setAllowedFields(ALLOWED_FIELDS);
	}

	@RequestMapping(value = "/packagingsByForm", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public List<SignaturePackaging> getAllowedPackagingsByForm(@RequestParam("form") SignatureForm signatureForm) {
		List<SignaturePackaging> packagings = new ArrayList<SignaturePackaging>();
		if (signatureForm != null) {
			switch (signatureForm) {
			case CAdES:
				packagings.add(SignaturePackaging.ENVELOPING);
				packagings.add(SignaturePackaging.DETACHED);
				break;
			case PAdES:
				packagings.add(SignaturePackaging.ENVELOPED);
				break;
			case XAdES:
				packagings.add(SignaturePackaging.ENVELOPED);
				packagings.add(SignaturePackaging.ENVELOPING);
				packagings.add(SignaturePackaging.DETACHED);
				packagings.add(SignaturePackaging.INTERNALLY_DETACHED);
				break;
			default:
				break;
			}
		}
		return packagings;
	}

	@RequestMapping(value = "/levelsByForm", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public List<SignatureLevel> getAllowedLevelsByForm(@RequestParam("form") SignatureForm signatureForm, @RequestParam("isSign") Boolean isSign) {
		List<SignatureLevel> levels = new ArrayList<SignatureLevel>();
		if (signatureForm != null) {
			switch (signatureForm) {
			case CAdES:
				if (Utils.isTrue(isSign)) {
					levels.add(SignatureLevel.CAdES_BASELINE_B);
				}
				levels.add(SignatureLevel.CAdES_BASELINE_T);
				levels.add(SignatureLevel.CAdES_BASELINE_LT);
				levels.add(SignatureLevel.CAdES_BASELINE_LTA);
				break;
			case PAdES:
				if (Utils.isTrue(isSign)) {
					levels.add(SignatureLevel.PAdES_BASELINE_B);
				}
				levels.add(SignatureLevel.PAdES_BASELINE_T);
				levels.add(SignatureLevel.PAdES_BASELINE_LT);
				levels.add(SignatureLevel.PAdES_BASELINE_LTA);
				break;
			case XAdES:
				if (Utils.isTrue(isSign)) {
					levels.add(SignatureLevel.XAdES_BASELINE_B);
				}
				levels.add(SignatureLevel.XAdES_BASELINE_T);
				levels.add(SignatureLevel.XAdES_BASELINE_LT);
				levels.add(SignatureLevel.XAdES_BASELINE_LTA);
				break;
			default:
				break;
			}
		}
		return levels;
	}
}
