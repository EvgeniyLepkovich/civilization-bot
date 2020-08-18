package com.civilization.configuration;

import com.civilization.model.*;
import com.civilization.repository.TableConfigurationRepository;
import com.civilization.repository.TableTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

@Configuration
public class TableConfigSeedDatabase {
    @Autowired
    private TableConfigurationRepository tableConfigurationRepository;
    @Autowired
    private TableTypeRepository tableTypeRepository;

    @Value("${spring.jpa.hibernate.ddl-auto}")
    private String ddlAutoValue;

    private static final TableConfiguration createdGameConfiguration;
    private static final TableConfiguration startedGameConfiguration;
    private static final TableConfiguration finishedGameConfiguration;
    private static final TableConfiguration declinedGameConfiguration;
    private static final TableConfiguration allUsersRatingConfiguration;

    private static final String CREATED_GAME_HEADER_TEMPLATE = "Game #{gameId} is ready to start, waiting all players";
    private static final String CREATED_GAME_COLUMN_TEMPLATE = "#|Player|G:W-L-R|Rolls|Ready";
    private static final String CREATED_GAME_USER_TEMPLATE = "{order}|{name}|{games}:{wins}-{leave}-{rating}|{rolls}|{ready}";

    private static final String STARTED_GAME_HEADER_TEMPLATE = "Game #{gameId} is started. Enjoy the game";
    private static final String STARTED_GAME_COLUMN_TEMPLATE = "#|Player|G:W-L-R|Rolls|Ready";
    private static final String STARTED_GAME_USER_TEMPLATE = "{order}|{name}|{games}:{wins}-{leave}-{rating}|{rolls}|{ready}";

    private static final String FINISHED_GAME_HEADER_TEMPLATE = "Game #{gameId} is finished. See result below";
    private static final String FINISHED_GAME_COLUMN_TEMPLATE = "#|Player|Result|Rating";
    private static final String FINISHED_GAME_USER_TEMPLATE = "{order}|{name}|{result}|{oldRating} -> {newRating}";

    private static final String DECLINED_GAME_HEADER_TEMPLATE = "Game #{gameId} is declined";
    private static final String DECLINED_GAME_COLUMN_TEMPLATE = "#|Player|G:W-L-R|Rolls|Ready";
    private static final String DECLINED_GAME_USER_TEMPLATE = "{order}|{name}|{games}:{wins}-{leave}-{rating}|{rolls}|{ready}";

    private static final String ALL_USERS_RATING_HEADER_TEMPLATE = "";
    private static final String ALL_USERS_RATING_COLUMN_TEMPLATE = "#|Player|Points-Games-Wins";
    private static final String ALL_USERS_RATING_BODY_TEMPLATE = "{place}|{name}|{rating}-{games}-{wins}";

    static {
        createdGameConfiguration = new TableConfiguration();
        createdGameConfiguration.setGridType(GridType.BORDER_DOUBLE);
        createdGameConfiguration.setWidthCalculatorType(WidthCalculatorType.CWC_LONGEST_LINE_WITH_HEADER_AND_FOOTER);
        createdGameConfiguration.setPaddingLeft(1);
        createdGameConfiguration.setPaddingRight(1);
        createdGameConfiguration.setHeaderRowTemplate(CREATED_GAME_HEADER_TEMPLATE);
        createdGameConfiguration.setColumnRowTemplate(CREATED_GAME_COLUMN_TEMPLATE);
        createdGameConfiguration.setUserRowTemplate(CREATED_GAME_USER_TEMPLATE);
    }

    static {
        startedGameConfiguration = new TableConfiguration();
        startedGameConfiguration.setGridType(GridType.BORDER_DOUBLE);
        startedGameConfiguration.setWidthCalculatorType(WidthCalculatorType.CWC_LONGEST_LINE_WITH_HEADER_AND_FOOTER);
        startedGameConfiguration.setPaddingLeft(1);
        startedGameConfiguration.setPaddingRight(1);
        startedGameConfiguration.setHeaderRowTemplate(STARTED_GAME_HEADER_TEMPLATE);
        startedGameConfiguration.setColumnRowTemplate(STARTED_GAME_COLUMN_TEMPLATE);
        startedGameConfiguration.setUserRowTemplate(STARTED_GAME_USER_TEMPLATE);
    }

    static {
        finishedGameConfiguration = new TableConfiguration();
        finishedGameConfiguration.setGridType(GridType.BORDER_DOUBLE);
        finishedGameConfiguration.setWidthCalculatorType(WidthCalculatorType.CWC_LONGEST_LINE_WITH_HEADER_AND_FOOTER);
        finishedGameConfiguration.setPaddingLeft(1);
        finishedGameConfiguration.setPaddingRight(1);
        finishedGameConfiguration.setHeaderRowTemplate(FINISHED_GAME_HEADER_TEMPLATE);
        finishedGameConfiguration.setColumnRowTemplate(FINISHED_GAME_COLUMN_TEMPLATE);
        finishedGameConfiguration.setUserRowTemplate(FINISHED_GAME_USER_TEMPLATE);
    }

    static {
        declinedGameConfiguration = new TableConfiguration();
        declinedGameConfiguration.setGridType(GridType.BORDER_DOUBLE);
        declinedGameConfiguration.setWidthCalculatorType(WidthCalculatorType.CWC_LONGEST_LINE_WITH_HEADER_AND_FOOTER);
        declinedGameConfiguration.setPaddingLeft(1);
        declinedGameConfiguration.setPaddingRight(1);
        declinedGameConfiguration.setHeaderRowTemplate(DECLINED_GAME_HEADER_TEMPLATE);
        declinedGameConfiguration.setColumnRowTemplate(DECLINED_GAME_COLUMN_TEMPLATE);
        declinedGameConfiguration.setUserRowTemplate(DECLINED_GAME_USER_TEMPLATE);
    }

    static {
        allUsersRatingConfiguration = new TableConfiguration();
        allUsersRatingConfiguration.setGridType(GridType.BORDER_DOUBLE);
        allUsersRatingConfiguration.setWidthCalculatorType(WidthCalculatorType.CWC_LONGEST_LINE_WITH_HEADER_AND_FOOTER);
        allUsersRatingConfiguration.setPaddingLeft(1);
        allUsersRatingConfiguration.setPaddingRight(1);
        allUsersRatingConfiguration.setHeaderRowTemplate(ALL_USERS_RATING_HEADER_TEMPLATE);
        allUsersRatingConfiguration.setColumnRowTemplate(ALL_USERS_RATING_COLUMN_TEMPLATE);
        allUsersRatingConfiguration.setUserRowTemplate(ALL_USERS_RATING_BODY_TEMPLATE);
    }

    @PostConstruct
    public void databaseSeed() {
        if ("create".equals(ddlAutoValue)) {
            tableTypeRepository.save(new TableType(TableTypeEnum.GAME_CREATED, createdGameConfiguration));
            tableTypeRepository.save(new TableType(TableTypeEnum.GAME_FINISHED, finishedGameConfiguration));
            tableTypeRepository.save(new TableType(TableTypeEnum.GAME_STARTED, startedGameConfiguration));
            tableTypeRepository.save(new TableType(TableTypeEnum.GAME_DECLINED, declinedGameConfiguration));
            tableTypeRepository.save(new TableType(TableTypeEnum.ALL_USERS_RATING, allUsersRatingConfiguration));
        }
    }
}
