package com.civilization.model;

public enum UserGameResult {
    WINNER {
        @Override
        public String getRussian() {
            return "����������";
        }
    }, ALIVE {
        @Override
        public String getRussian() {
            return "��������";
        }
    }, LEAVE {
        @Override
        public String getRussian() {
            return "�����";
        }
    }, DESTROYED {
        @Override
        public String getRussian() {
            return "���������";
        }
    }, NOT_DETECTED;

    public String getRussian() {
        return "";
    }
}
