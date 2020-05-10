package com.civilization.model;

public enum UserGameResult {
    WINNER {
        @Override
        public String getRussian() {
            return "ПОБЕДИТЕЛЬ";
        }
    }, ALIVE {
        @Override
        public String getRussian() {
            return "ВЫЖИВШИЙ";
        }
    }, LEAVE {
        @Override
        public String getRussian() {
            return "ЛИВЕР";
        }
    }, DESTROYED {
        @Override
        public String getRussian() {
            return "УНИЧТОЖЕН";
        }
    }, NOT_DETECTED;

    public String getRussian() {
        return "";
    }
}
