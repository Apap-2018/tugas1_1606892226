package com.apap.tugas1.service;

import java.util.ArrayList;
import java.util.Optional;

import com.apap.tugas1.model.JabatanModel;

public interface JabatanService {
	void addJabatan(JabatanModel jabatan);
	Optional <JabatanModel> getJabatanDetailById(Long id);
	void deleteJabatan(JabatanModel jabatan);
	ArrayList<JabatanModel> getListJabatan();



}