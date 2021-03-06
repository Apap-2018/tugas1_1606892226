package com.apap.tugas1.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.*;

import com.apap.tugas1.model.JabatanModel;
import com.apap.tugas1.repository.JabatanDB;

@Service
@Transactional
public class JabatanServiceImpl implements JabatanService {
	@Autowired
	private JabatanDB jabatanDB;

	@Override
	public void addJabatan(JabatanModel jabatan) {
		// TODO Auto-generated method stub
		jabatanDB.save(jabatan);
	}

	
	@Override
	public Optional<JabatanModel> getJabatanDetailById(Long id){
		return jabatanDB.findById(id);
	}

	@Override
	public void deleteJabatan( JabatanModel jabatan) {
		jabatanDB.deleteById(jabatan.getId());
	}

	@Override
	public ArrayList<JabatanModel> getListJabatan() {
		// TODO Auto-generated method stub
		return (ArrayList<JabatanModel>) jabatanDB.findAll();
	}

}