package com.apap.tugas1.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.apap.tugas1.model.JabatanModel;
import com.apap.tugas1.model.PegawaiModel;
import com.apap.tugas1.repository.PegawaiDB;

@Service
@Transactional
public class PegawaiServiceImpl implements PegawaiService {
	@Autowired
	private PegawaiDB pegawaiDB;

	@Override
	public PegawaiModel getPegawaiDetailByNip(String nip) {
		return pegawaiDB.findByNip(nip);
	}

//	@Override
//	public double getGajiLengkapByNip(String nip) {
//		double gajiLengkap = 0;
//		PegawaiModel pegawai = this.getPegawaiDetailByNip(nip);
//		double gajiTerbesar = 0;
//		for (JabatanModel jabatan:pegawai.getJabatanList()) {
//			if (jabatan.getGajiPokok() > gajiTerbesar) {
//				gajiTerbesar = jabatan.getGajiPokok();
//			}
//		}
//		System.out.println("Pokok: " + gajiTerbesar);
//		gajiLengkap += gajiTerbesar;
//		double presentaseTunjangan = pegawai.getInstansi().getProvinsi().getPresentaseTunjangan();
//		System.out.println("Presentase: " + presentaseTunjangan);
//		gajiLengkap += (gajiLengkap * presentaseTunjangan/100);
//		System.out.println("Lengkap: " + gajiLengkap);
//		return gajiLengkap;
//	}

	//@Override
	//public long countEntity() {
		//return pegawaiDB.count();
	//}

	//@Override
	//public PegawaiModel getPegawaiDetailById(Long id) {
		//return pegawaiDB.getOne(id);
	//}

}
