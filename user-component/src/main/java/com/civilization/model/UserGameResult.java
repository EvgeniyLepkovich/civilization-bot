package com.civilization.model;

public enum UserGameResult {
    WINNER {
        @Override
        public String getRussian() {
            return "онаедхрекэ";
        }
    }, ALIVE {
        @Override
        public String getRussian() {
            return "бшфхбьхи";
        }
    }, LEAVE {
        @Override
        public String getRussian() {
            return "кхбеп";
        }
    }, DESTROYED {
        @Override
        public String getRussian() {
            return "смхврнфем";
        }
    }, NOT_DETECTED;

    public String getRussian() {
        return "";
    }
}
