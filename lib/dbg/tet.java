/*--------------------------------------------------------------------------*/
/*  Copyright 2015 Sergey Vostokin, Ekaterina Skoryupina                    */
/*                                                                          */
/*  Licensed under the Apache License, Version 2.0 (the "License");         */
/*  you may not use this file except in compliance with the License.        */
/*  You may obtain a copy of the License at                                 */
/*                                                                          */
/*  http://www.apache.org/licenses/LICENSE-2.0                              */
/*                                                                          */
/*  Unless required by applicable law or agreed to in writing, software     */
/*  distributed under the License is distributed on an "AS IS" BASIS,       */
/*  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.*/
/*  See the License for the specific language governing permissions and     */
/*  limitations under the License.                                          */
/*--------------------------------------------------------------------------*/

/* initial C++ runtime*/

package dbg;

import java.util.ArrayList;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.Condition;
import java.util.Queue;

public class tet {

/*
struct engine{
	std::vector<chan*> ready;
};
*/

    class engine {
        private ArrayList<chan> ready;
    }

/*
struct proc{
    void(*recv)(chan*, proc*);
};*/

    abstract class proc {
        public abstract void recv(chan chan, proc proc);
    }

/*
struct chan{
    proc*p;
    bool sending;
};*/

    class chan {
        private proc p;
        private boolean sending;
    }

/*inline void duration(engine*e, double t){}*/

    public void duration(engine e, double t) {
    }

/*
inline void send(engine*e, chan*c, proc*p) {
    if (c -> sending) return;
    c -> sending = true;
    c -> p = p;
    e -> ready.push_back(c);
}*/

    public void send(engine e, chan c, proc p) {
        if (c.sending) {
            return;
        }
        c.sending = true;
        c.p = p;
        e.ready.add(c);
    }
/*
inline bool access(chan*c, proc*p) {
    return c -> p == p && !c -> sending;
}*/

    public boolean access(chan c, proc p) {
        return c.p == p && !c.sending;
    }

/*inline void run(engine*e, int n=1) {
    size_t rsize;
    while (rsize = e -> ready.size()) {
        int n = rand() % rsize;
        auto it = e -> ready.begin() + n;
        chan * c =*it;
        e -> ready.erase(it);
        c -> sending = false;
        c -> p -> recv(c, c -> p);
    }
}*/

    public void run(engine e) {
        final int RAND_MAX = 32767;
        long rsize = 0;
        while (rsize == e.ready.size()) {
            int n = (int) ((Math.random() * RAND_MAX) % rsize);
            chan it = (chan) e.ready.iterator(); //+ n; //TODO не ясна конструкция
            chan c = it;
            e.ready.remove(it);
            c.sending=false;
            c.p.recv(c,c.p);
        }
    }


/*inline void stat(engine*e, double&T1, double&Tp, int&Pmax, double&Smax, int P, double&Sp) {
}*/

    void stat(engine e, double T1, double Tp, int Pmax, double Smax, int P, double Sp) {
    }
}
