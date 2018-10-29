package com.apap.tugas1.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.apap.tugas1.model.InstansiModel;
import com.apap.tugas1.model.JabatanModel;
import com.apap.tugas1.model.PegawaiModel;
import com.apap.tugas1.service.InstansiService;
import com.apap.tugas1.service.JabatanService;
import com.apap.tugas1.service.PegawaiService;
import com.apap.tugas1.service.ProvinsiService;;



@Controller
public class PegawaiController {
	@Autowired
	private PegawaiService pegawaiService;
	
	@Autowired
	private ProvinsiService provinsiService;
	
	@Autowired
	private InstansiService instansiService;

	@Autowired
	private JabatanService jabatanService;
	
	@RequestMapping("/")
	private String index(Model model) {
		List<JabatanModel> archive = jabatanService.getListJabatan();
		model.addAttribute("listJabatan", archive);
		List<InstansiModel> listInstansi = instansiService.getInstansiList();
		model.addAttribute("listInstansi", listInstansi);
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
	 
	 
	 @RequestMapping("/pegawai/tambah")
		private String addPegawai(Model model) {
			PegawaiModel pegawai = new PegawaiModel();
			pegawai.setJabatanList(new ArrayList<JabatanModel>());
			pegawai.getJabatanList().add(new JabatanModel());
			model.addAttribute("pegawai", pegawai);
			model.addAttribute("listProvinsi", provinsiService.getProvinsiList());
			model.addAttribute("listInstansi", instansiService.getInstansiList());
			model.addAttribute("listJabatan", jabatanService.getListJabatan());
			return "add-pegawai";
		}
		
		@PostMapping(value = "/pegawai/tambah", params= {"pegawaiSubmit"})
	    public String submitAddPegawai(@ModelAttribute PegawaiModel pegawai,Model model) {
			String nip = "";
			
			nip += pegawai.getInstansi().getId();
			
			String[] tglLahir = pegawai.getTanggalLahir().toString().split("-");
			String tglLahirString = tglLahir[2] + tglLahir[1] + tglLahir[0].substring(2, 4);
			nip += tglLahirString;

			nip += pegawai.getTahunMasuk();

			int counterSama = 1;
			for (PegawaiModel pegawaiInstansi:pegawai.getInstansi().getPegawaiInstansi()) {
				if (pegawaiInstansi.getTahunMasuk().equals(pegawai.getTahunMasuk()) && pegawaiInstansi.getTanggalLahir().equals(pegawai.getTanggalLahir())) {
					counterSama += 1;
				}	
			}
			nip += "0" + counterSama;

			for (JabatanModel jabatan:pegawai.getJabatanList()) {
				System.out.println(jabatan.getNama());
			}
			pegawai.setNip(nip);
			pegawaiService.addPegawai(pegawai);
	        return "add-pegawaiSukses";
	    }
		
		@PostMapping(value = "/pegawai/tambah", params= {"addRow"})
	    public String addRowJabatan(@ModelAttribute PegawaiModel pegawai, BindingResult bindingResult, Model model) {
	    	if (pegawai.getJabatanList()== null) {
				pegawai.setJabatanList(new ArrayList<JabatanModel>());
			}
	    	pegawai.getJabatanList().add(new JabatanModel());
	    	model.addAttribute("pegawai", pegawai);
	    	System.out.println(pegawai.getJabatanList().size());
	        model.addAttribute("listJabatan", jabatanService.getListJabatan());
	        model.addAttribute("listInstansi", instansiService.getInstansiList());
	        model.addAttribute("listProvinsi", provinsiService.getProvinsiList());
			return "add-pegawai";
	    }
		
		@PostMapping(value="/pegawai/tambah",params= {"deleteRow"})
		private String deleteRowJabatan(@ModelAttribute PegawaiModel pegawai,BindingResult bindingResult, Model model, HttpServletRequest req) {
			final Integer rowId = Integer.valueOf(req.getParameter("deleteRow"));
		    pegawai.getJabatanList().remove(rowId.intValue());
		    model.addAttribute("pegawai", pegawai);
		    model.addAttribute("listJabatan", jabatanService.getListJabatan());
		    model.addAttribute("listInstansi", instansiService.getInstansiList());
	        model.addAttribute("listProvinsi", provinsiService.getProvinsiList());
			return "add-pegawai";
		}
	
	@RequestMapping("/pegawai/tertua-termuda")
	private String pegawaiTertuaTermuda(@RequestParam(value="idInstansi", required = true) Long id, Model model) {
		InstansiModel instansi = instansiService.getInstansiById(id);
		if (instansi==null) {
			return "kosong";
		}
		else {
			List<PegawaiModel> pegawais = instansi.getPegawaiInstansi();
			if (pegawais.isEmpty()) {
				return "kosong";
			}
			PegawaiModel tertua = pegawais.get(0);
			PegawaiModel termuda = pegawais.get(0);
			for (PegawaiModel pegawai : pegawais) {
				if (pegawai.getTanggalLahir().compareTo(tertua.getTanggalLahir())<0) {
					tertua = pegawai;
				}
				if (pegawai.getTanggalLahir().compareTo(termuda.getTanggalLahir())>0) {
					termuda = pegawai;
				}
			}
			model.addAttribute("tertua", tertua);
			model.addAttribute("termuda",termuda);
			return "tua-muda";
		}
	}

	@RequestMapping("/pegawai/cari")
	private String cariPegawai(@RequestParam(value="idProvinsi",required=false) String idProvinsi,@RequestParam(value="idInstansi",required=false) String idInstansi,@RequestParam(value="idJabatan",required=false) String idJabatan,Model model) {
		model.addAttribute("listProvinsi", provinsiService.getProvinsiList());
		model.addAttribute("listInstansi", instansiService.getInstansiList());
		model.addAttribute("listJabatan", jabatanService.getListJabatan());
		List<PegawaiModel> pegawai = pegawaiService.getPegawaiList();
		
		if ((idProvinsi==null || idProvinsi.equals("")) && (idInstansi==null||idInstansi.equals("")) && (idJabatan==null||idJabatan.equals(""))) {
		}
		else {
			if (idProvinsi!=null && !idProvinsi.equals("")) {
				List<PegawaiModel> temp = new ArrayList<PegawaiModel>();
				for (PegawaiModel peg: pegawai) {
					if (((Long)peg.getInstansi().getProvinsi().getId()).toString().equals(idProvinsi)) {
						temp.add(peg);
					}
				}
				pegawai = temp;
				model.addAttribute("idProvinsi", Long.parseLong(idProvinsi));
			}
			else {
				model.addAttribute("idProvinsi", "");
			}
			if (idInstansi!=null&&!idInstansi.equals("")) {
				List<PegawaiModel> temp = new ArrayList<PegawaiModel>();
				for (PegawaiModel peg: pegawai) {
					if (((Long)peg.getInstansi().getId()).toString().equals(idInstansi)) {
						temp.add(peg);
					}
				}
				pegawai = temp;
				model.addAttribute("idInstansi", Long.parseLong(idInstansi));
			}
			else {
				model.addAttribute("idInstansi", "");
			}
			if (idJabatan!=null&&!idJabatan.equals("")) {
				List<PegawaiModel> temp = new ArrayList<PegawaiModel>();
				for (PegawaiModel peg: pegawai) {
					for (JabatanModel jabatan:peg.getJabatanList()) {
						if (((Long)jabatan.getId()).toString().equals(idJabatan)) {
							temp.add(peg);
							break;
						}
					}
					
				}
				pegawai = temp;
				model.addAttribute("idJabatan", Long.parseLong(idJabatan));
			}
			else {
				model.addAttribute("idJabatan", "");
			}
		}
		model.addAttribute("listPegawai",pegawai);
		return "cari-pegawai";
	}
	
	@RequestMapping("/pegawai/ubah")
	private String editPegawai(@RequestParam(value="nip", required = true) String nip, Model model) {
		PegawaiModel pegawai = pegawaiService.getPegawaiDetailByNip(nip);
		if (pegawai==null) {
			return "404";
		}
		if(pegawai.getJabatanList().size()==0) {
			pegawai.getJabatanList().add(new JabatanModel());
		}
		model.addAttribute("pegawai", pegawai);
		model.addAttribute("listProvinsi", provinsiService.getProvinsiList());
		model.addAttribute("listInstansi", instansiService.getInstansiList());
		model.addAttribute("listJabatan", jabatanService.getListJabatan());
		return "ubah-pegawai";
	}
	
	@PostMapping(value = "/pegawai/ubah", params= {"pegawaiSubmit"})
    public String submitEditPegawai(@ModelAttribute PegawaiModel pegawai,Model model) {
		pegawaiService.addPegawai(pegawai);
		String nip = "";
		
		nip += pegawai.getInstansi().getId();
		
		String[] tglLahir = pegawai.getTanggalLahir().toString().split("-");
		String tglLahirString = tglLahir[2] + tglLahir[1] + tglLahir[0].substring(2, 4);
		nip += tglLahirString;

		nip += pegawai.getTahunMasuk();

		int counterSama = 1;
		for (PegawaiModel pegawaiInstansi:pegawai.getInstansi().getPegawaiInstansi()) {
			if (pegawaiInstansi.getTahunMasuk().equals(pegawai.getTahunMasuk()) && pegawaiInstansi.getTanggalLahir().equals(pegawai.getTanggalLahir())) {
				counterSama += 1;
			}	
		}
		nip += "0" + counterSama;

		for (JabatanModel jabatan:pegawai.getJabatanList()) {
			System.out.println(jabatan.getNama());
		}
		pegawai.setNip(nip);
		pegawaiService.addPegawai(pegawai);
		model.addAttribute("pesan", "Pegawai NIP "+nip+" berhasil diubah");
        return "ubah-pegawaiSukses";
    }
	
	@PostMapping(value = "/pegawai/ubah", params= {"addRow"})
    public String addRowJabatanEdit(@ModelAttribute PegawaiModel pegawai, BindingResult bindingResult, Model model) {
    	if (pegawai.getJabatanList()== null) {
			pegawai.setJabatanList(new ArrayList<JabatanModel>());
		}
    	pegawai.getJabatanList().add(new JabatanModel());
    	model.addAttribute("pegawai", pegawai);
    	System.out.println(pegawai.getJabatanList().size());
        model.addAttribute("listJabatan", jabatanService.getListJabatan());
        model.addAttribute("listInstansi", instansiService.getInstansiList());
        model.addAttribute("listProvinsi", provinsiService.getProvinsiList());
		return "ubah-pegawai";
    }
	
	@PostMapping(value="/pegawai/ubah",params= {"deleteRow"})
	private String deleteRowJabatanUbah(@ModelAttribute PegawaiModel pegawai,BindingResult bindingResult, Model model, HttpServletRequest req) {
		final Integer rowId = Integer.valueOf(req.getParameter("deleteRow"));
	    pegawai.getJabatanList().remove(rowId.intValue());
	    model.addAttribute("pegawai", pegawai);
	    model.addAttribute("listJabatan", jabatanService.getListJabatan());
	    model.addAttribute("listInstansi", instansiService.getInstansiList());
        model.addAttribute("listProvinsi", provinsiService.getProvinsiList());
		return "ubah-pegawai";
	}
}
