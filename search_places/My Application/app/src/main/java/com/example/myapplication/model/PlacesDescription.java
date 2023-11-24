package com.example.myapplication.model;

import java.util.List;

public class PlacesDescription {
    private String type;
    private List<Feature> features;

    public String getType() {
        return type;
    }

    public List<Feature> getFeatures() {
        return features;
    }

    @Override
    public String toString() {
        return "PlacesDescription{" +
                "features=" + features +
                '}';
    }

    public static class Feature {
        private String type;
        private String id;

        @Override
        public String toString() {
            return "Feature{" +
                    "type='" + type + '\'' +
                    ", id='" + id + '\'' +
                    ", geometry=" + geometry +
                    ", properties=" + properties +
                    '}';
        }

        private Geometry geometry;
        private Properties properties;

        public String getType() {
            return type;
        }

        public String getId() {
            return id;
        }

        public Geometry getGeometry() {
            return geometry;
        }

        public Properties getProperties() {
            return properties;
        }
    }

    public static class Geometry {
        private String type;
        private List<Double> coordinates;

        public String getType() {
            return type;
        }

        public List<Double> getCoordinates() {
            return coordinates;
        }
    }

    public static class Properties {
        private String xid;
        private String name;

        @Override
        public String toString() {
            return "Properties{" +
                    "name='" + name + '\'' +
                    ", dist=" + dist +
                    ", rate=" + rate +
                    ", kinds='" + kinds + '\'' +
                    '}';
        }

        private double dist;
        private int rate;
        private String osm;
        private String wikidata;
        private String kinds;

        public String getXid() {
            return xid;
        }

        public String getName() {
            return name;
        }

        public double getDist() {
            return dist;
        }

        public int getRate() {
            return rate;
        }

        public String getOsm() {
            return osm;
        }

        public String getWikidata() {
            return wikidata;
        }

        public String getKinds() {
            return kinds;
        }
    }
}
