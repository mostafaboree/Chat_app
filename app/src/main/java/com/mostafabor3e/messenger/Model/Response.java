package com.mostafabor3e.messenger.Model;

import java.util.List;

public class Response {
        private  double multicast_id;
        private  int success;
        private  int failure;
        private  int conanicen_id;
        private List<Result> ruselts;

        public Response(double multicast_id, int success, int failure, int conanicen_id, List<Result> ruselts) {
            this.multicast_id = multicast_id;
            this.success = success;
            this.failure = failure;
            this.conanicen_id = conanicen_id;
            this.ruselts = ruselts;
        }

        public Response() {
        }

}
