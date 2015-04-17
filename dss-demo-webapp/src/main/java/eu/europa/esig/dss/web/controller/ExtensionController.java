package eu.europa.esig.dss.web.controller;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;

import eu.europa.esig.dss.DSSDocument;
import eu.europa.esig.dss.InMemoryDocument;
import eu.europa.esig.dss.SignatureLevel;
import eu.europa.esig.dss.signature.SignaturePackaging;
import eu.europa.esig.dss.web.editor.EnumPropertyEditor;
import eu.europa.esig.dss.web.model.ExtensionForm;
import eu.europa.esig.dss.web.service.SigningService;
import eu.europa.esig.dss.x509.SignatureForm;

@Controller
@RequestMapping(value = "/extension")
public class ExtensionController {

	private static final Logger logger = LoggerFactory.getLogger(ExtensionController.class);

	private static final String EXTENSION_TILE = "extension";

	@Autowired
	private SigningService signingService;

	@InitBinder
	public void initBinder(WebDataBinder binder) {
		binder.registerCustomEditor(SignatureForm.class, new EnumPropertyEditor(SignatureForm.class));
		binder.registerCustomEditor(SignaturePackaging.class, new EnumPropertyEditor(SignaturePackaging.class));
		binder.registerCustomEditor(SignatureLevel.class, new EnumPropertyEditor(SignatureLevel.class));
	}

	@RequestMapping(method = RequestMethod.GET)
	public String showExtension(Model model, HttpServletRequest request) {
		model.addAttribute("extensionForm", new ExtensionForm());
		return EXTENSION_TILE;
	}

	@RequestMapping(method = RequestMethod.POST)
	public String extend(HttpServletRequest request, HttpServletResponse response, @ModelAttribute("extensionForm") @Valid ExtensionForm extensionForm, BindingResult result) {
		if (result.hasErrors()) {
			return EXTENSION_TILE;
		}

		DSSDocument toExtendDocument = toDSSDocument(extensionForm.getSignedFile());
		DSSDocument extendedDocument = signingService.extend(extensionForm.getSignatureForm(), extensionForm.getSignaturePackaging(), extensionForm.getSignatureLevel(), toExtendDocument, toDSSDocument(extensionForm.getOriginalFile()));

		String originalName = toExtendDocument.getName();
		String extendedFileName = StringUtils.substringBeforeLast(originalName, ".") + "-extended."+StringUtils.substringAfterLast(originalName, ".");

		response.setContentType(extendedDocument.getMimeType().getMimeTypeString());
		response.setHeader("Content-Disposition", "attachment; filename=" + extendedFileName);
		try {
			IOUtils.copy(new ByteArrayInputStream(extendedDocument.getBytes()), response.getOutputStream());
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}

		return null;
	}

	private DSSDocument toDSSDocument(MultipartFile multipartFile) {
		try {
			if ((multipartFile != null) && !multipartFile.isEmpty()) {
				DSSDocument document = new InMemoryDocument(multipartFile.getBytes(), multipartFile.getOriginalFilename());
				return document;
			}
		} catch (IOException e) {
			logger.error("Cannot read  file : " + e.getMessage(), e);
		}
		return null;
	}

	@ModelAttribute("signatureForms")
	public SignatureForm[] getSignatureForms() {
		return SignatureForm.values();
	}

	@ModelAttribute("signaturePackagings")
	public SignaturePackaging[] getSignaturePackagings() {
		return SignaturePackaging.values();
	}

	@ModelAttribute("signatureLevels")
	public SignatureLevel[] getSignatureLevels() {
		return SignatureLevel.values();
	}

}
