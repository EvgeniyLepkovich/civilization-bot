package com.civilization.service.impl;

import com.civilization.model.Nation;
import com.civilization.repository.NationRepository;
import com.civilization.service.NationRollService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NationRollServiceImpl implements NationRollService {

    @Autowired
    private NationRepository nationRepository;

    @Override
    public List<Nation> ffaSixRoll() {
        return nationRepository.getFFA6RandomNation();
    }
}
