package com.apap.tugas1.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.apap.tugas1.model.InstansiModel;
import com.apap.tugas1.model.JabatanModel;
import com.apap.tugas1.model.PegawaiModel;
import com.apap.tugas1.model.ProvinsiModel;
import com.apap.tugas1.service.InstansiService;
import com.apap.tugas1.service.PegawaiService;
import com.apap.tugas1.service.ProvinsiService;


@Controller
public class PegawaiController {
	@Autowired
	private PegawaiService pegawaiService;
	
	@Autowired
	private ProvinsiService provinsiService;
	
	@Autowired
	private InstansiService instansiService;
	
	@RequestMapping("/")
	private String index() {
		return "index";
	}
	
	
	 @RequestMapping("/pegawai")
	 private String viewPegawai(@RequestParam(value="nip", required = true) String nip, Model model) {
	  PegawaiModel pegawai = pegawaiService.getPegawaiDetailByNip(nip);
	  model.addAttribute("pegawai", pegawai);
	  double gaji = 0;
	  double gajiTerbesar = 0;
	        for (JabatanModel jabatan : pegawai.getJabatanList()) {
	            if (jabatan.getGajiPokok() > gajiTerbesar) {
	                gajiTerbesar = jabatan.getGajiPokok();
	            }
	        }
	        gaji = gajiTerbesar;
	        double tunjangan = pegawai.getInstansi().getProvinsi().getPresentaseTunjangan();
	        gaji += (gaji * tunjangan/100);
	        model.addAttribute("gajiPegawai",gaji);
	        
	  return "view-pegawai";
	 }
	 
	 
	@RequestMapping(value = "/pegawai/tambah", method = RequestMethod.GET)
	private String addPegawai(Model model) {
		model.addAttribute("listProvinsi", provinsiService.getProvinsiList());
		model.addAttribute("pegawai", new PegawaiModel());
		//model.addAttribute("instansi", new InstansiModel());
		
		return "tambah-pegawai";
	}
	
	@RequestMapping(value = "/pegawai/tambah", method = RequestMethod.POST)
	private String addPegwawaiSubmit(@ModelAttribute PegawaiModel pegawai) {
		System.out.println("");
		System.out.println(pegawai.getNama());
		System.out.println(pegawai.getInstansi()); //Error Masih Null
		//System.out.println(pegawai.getInstansi().getNama());
		//System.out.println(pegawai.getInstansi().getProvinsi().getNama());
		return "index";
	}
	
	@RequestMapping(value = "/pegawai/tambah/instansi", method = RequestMethod.GET)
	public @ResponseBody List<InstansiModel> findAllInstansi(@RequestParam(value = "provinsiId", required = true) long provinsiId) {
	    ProvinsiModel provinsi = provinsiService.getProvinsiDetailById(provinsiId);
	    for (InstansiModel instansi:provinsi.getInstansiList()) {
	    	System.out.println(instansi.getNama());
	    }
	    return provinsi.getInstansiList(); 
	}
	
}
