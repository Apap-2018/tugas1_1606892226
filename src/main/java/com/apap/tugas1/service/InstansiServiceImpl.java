package com.apap.tugas1.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.apap.tugas1.model.InstansiModel;
import com.apap.tugas1.repository.InstansiDB;

@Service
@Transactional
public class InstansiServiceImpl implements InstansiService {
	@Autowired
	private InstansiDB InstansiDB;

	@Override
	public List<InstansiModel> getInstansiList() {
		return InstansiDB.findAll();
	}

	@Override
	public InstansiModel getInstansiById(long id) {
		// TODO Auto-generated method stub
		return InstansiDB.findById(id).get();
	}

}