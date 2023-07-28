package com.thegiantgames.splitpay;

public class Group {



        private  int ImageView;
        private String groupName , owe;

        public int getImageView() {
            return ImageView;
        }

        public void setImageView(int imageView) {
            ImageView = imageView;
        }

        public String getGroupName() {
            return groupName;
        }

        public void setGroupName(String groupName) {
            this.groupName = groupName;
        }

        public String getOwe() {
            return owe;
        }

        public void setOwe(String owe) {
            this.owe = owe;
        }

        public Group(int imageView, String groupName, String owe) {
            ImageView = imageView;
            this.groupName = groupName;
            this.owe = owe;
        }
    }



