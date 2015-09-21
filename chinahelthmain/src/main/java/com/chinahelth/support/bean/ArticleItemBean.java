package com.chinahelth.support.bean;

/**
 * Created by caihanyuan on 15-7-20.
 */
public class ArticleItemBean {

    public String articleId;

    public int groupType;

    public int itemType;

    public String title;

    public String from;

    public int commentNums;

    public long publishTime;

    public String[] thumbnailUris;

    public boolean isReaded = false;

    private String thumbnailJson = null;

    private ItemKey key = null;

    public ItemKey getKey() {
        if (key == null)
            key = new ItemKey(this.articleId, this.publishTime);
        return key;
    }

    public String getThumbnailJson() {
        if (thumbnailJson == null) {
            if (thumbnailUris != null) {
                int length = thumbnailUris.length;
                StringBuffer content = new StringBuffer();
                content.append('[');
                for (int i = 0; i < length; i++) {
                    content.append('"');
                    content.append(thumbnailUris[i]);
                    content.append('"');
                    if (i != length - 1) {
                        content.append(',');
                    }
                }
                content.append(']');
                thumbnailJson = content.toString();
            }
        }
        return thumbnailJson;
    }

    public static class ItemKey implements Comparable<ItemKey> {

        public String id;

        public long publishTime;

        public ItemKey(String id, long publishTime) {
            this.id = id;
            this.publishTime = publishTime;
        }

        /**
         * Des sort by publishTime
         *
         * @param another
         * @return
         */
        @Override
        public int compareTo(ItemKey another) {
            if (this.publishTime > another.publishTime)
                return -1;
            else if (this.publishTime < another.publishTime)
                return 1;
            else
                return 0;
        }

        @Override
        public boolean equals(Object o) {
            ItemKey otherKey = null;
            if (o instanceof ItemKey) {
                otherKey = (ItemKey) o;
                return this.id.equals(otherKey.id);
            } else {
                return false;
            }
        }
    }
}
