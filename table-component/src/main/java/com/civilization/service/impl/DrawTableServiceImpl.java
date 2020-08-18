package com.civilization.service.impl;

import com.civilization.dto.GameResultDTO;
import com.civilization.dto.UserDTO;
import com.civilization.model.TableConfiguration;
import com.civilization.model.TableTypeEnum;
import com.civilization.model.UserRank;
import com.civilization.repository.TableConfigurationRepository;
import com.civilization.service.DrawTableService;
import com.civilization.util.AllUsersRatingTableSchemeSettingMapper;
import com.civilization.util.DefaultTableSchemeSettingMapper;
import com.civilization.util.GameFinishedTableSchemeSettingMapper;
import com.civilization.util.TableRender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DrawTableServiceImpl implements DrawTableService {

    @Autowired
    private TableRender tableRender;
    @Autowired
    private TableConfigurationRepository tableConfigurationRepository;
    @Autowired
    private DefaultTableSchemeSettingMapper defaultSchemeTableSettingMapper;
    @Autowired
    private GameFinishedTableSchemeSettingMapper gameFinishedTableSchemeSettingMapper;
    @Autowired
    private AllUsersRatingTableSchemeSettingMapper allUsersRatingTableSchemeSettingMapper;

    @Override
    public String drawGameTable(List<UserDTO> users, Long gameId) {
        TableConfiguration tableConfiguration = tableConfigurationRepository.getTableConfigurationBySchemeType(TableTypeEnum.GAME_CREATED.name());
        return tableRender.renderDefaultGameTable(defaultSchemeTableSettingMapper.toSetting(users, gameId, tableConfiguration));
    }

    @Override
    public String drawGameStartedTable(List<UserDTO> users, Long gameId) {
        TableConfiguration tableConfiguration = tableConfigurationRepository.getTableConfigurationBySchemeType(TableTypeEnum.GAME_STARTED.name());
        return tableRender.renderDefaultGameTable(defaultSchemeTableSettingMapper.toSetting(users, gameId, tableConfiguration));
    }

    @Override
    public String drawGameFinishedTable(List<GameResultDTO> gameResultDTOs, Long gameId) {
        TableConfiguration tableConfiguration = tableConfigurationRepository.getTableConfigurationBySchemeType(TableTypeEnum.GAME_FINISHED.name());
        return tableRender.renderDefaultGameTable(gameFinishedTableSchemeSettingMapper.toSetting(gameResultDTOs, gameId, tableConfiguration));
    }

    @Override
    public String drawGameDeclinedTable(List<UserDTO> users, Long gameId) {
        TableConfiguration tableConfiguration = tableConfigurationRepository.getTableConfigurationBySchemeType(TableTypeEnum.GAME_DECLINED.name());
        return tableRender.renderDefaultGameTable(defaultSchemeTableSettingMapper.toSetting(users, gameId, tableConfiguration));
    }

    @Override
    public String drawAllUsersRatingTable(List<UserRank> userRanks) {
        TableConfiguration tableConfiguration = tableConfigurationRepository.getTableConfigurationBySchemeType(TableTypeEnum.ALL_USERS_RATING.name());
        return tableRender.renderDefaultGameTable(allUsersRatingTableSchemeSettingMapper.toSetting(userRanks, tableConfiguration));
    }
}
