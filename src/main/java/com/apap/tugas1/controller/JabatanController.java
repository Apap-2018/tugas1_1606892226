package com.apap.tugas1.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.apap.tugas1.model.JabatanModel;
import com.apap.tugas1.service.JabatanPegawaiService;
import com.apap.tugas1.service.JabatanService;
import com.apap.tugas1.service.PegawaiService;
import com.apap.tugas1.service.ProvinsiService;

@Controller
public class JabatanController {
	@Autowired
	private PegawaiService pegawaiService;
	
	@Autowired
	private ProvinsiService provinsiService;
	
	@Autowired
	private JabatanPegawaiService jabatanPegawaiService;
	
	@Autowired
	private JabatanService jabatanService;
	
	@RequestMapping(value="/jabatan/tambah",method=RequestMethod.GET)
	private String add(Model model) {
		model.addAttribute("jabatan",new JabatanModel());
		return "addJabatan";
	}
	
	@RequestMapping(value="/jabatan/tambah",method=RequestMethod.POST)
	private String addJabatan(@ModelAttribute JabatanModel jabatan) {
		jabatanService.addJabatan(jabatan);
		return "add";
	}

	@RequestMapping(value = "/jabatan/view")
    private String viewJabatan(@RequestParam(value="id") Long id, Model model) {
        JabatanModel jabatan = jabatanService.getJabatanDetailById(id).get();
        model.addAttribute("jabatan", jabatan);
        return "view-jabatan";
	}
	
	@RequestMapping(value = "/jabatan/hapus", method = RequestMethod.POST)
	private String hapusJabatanGet(@ModelAttribute JabatanModel jabatan, Model model){
		jabatanService.deleteJabatan(jabatan);		
		return "delete";
	}

	@RequestMapping(value="/jabatan/ubah", method = RequestMethod.GET)
	private String ubahJabatan(@RequestParam(value = "id") long id, Model model) {
		JabatanModel jabatan = jabatanService.getJabatanDetailById(id).get();
		model.addAttribute("jabatan", jabatan);
		return "ubah-jabatan";
	}
	@RequestMapping(value = "/jabatan/ubah", method = RequestMethod.POST)
	private String ubahJabatanSubmit(@ModelAttribute JabatanModel jabatan, Model model) {
		jabatanService.addJabatan(jabatan);
		return "add";
	}


 

}