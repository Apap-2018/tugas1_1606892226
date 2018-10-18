package com.apap.tugas1.service;

import java.util.List;

import com.apap.tugas1.model.JabatanPegawaiModel;

public interface JabatanPegawaiService {
	List<JabatanPegawaiModel> getJabatanByPegawaiId(long nip);
	long sizeJabatanPegawai();
	JabatanPegawaiModel checkWho(long id);
}
