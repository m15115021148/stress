package com.meigsmart.slb767_stress.model;

import java.util.List;

/**
 * Created by chenMeng on 2018/5/21.
 */
public class LoopModel {
    private String name;
    private List<LoopSubModel> list;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<LoopSubModel> getList() {
        return list;
    }

    public void setList(List<LoopSubModel> list) {
        this.list = list;
    }

    public static class LoopSubModel{
        private String name;
        private int time;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getTime() {
            return time;
        }

        public void setTime(int time) {
            this.time = time;
        }
    }
}
