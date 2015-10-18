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

/* initial C++ runtime

package par;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.Condition;
import java.util.Queue;

public class tet {
    /*
    struct engine{
        volatile int active;
        std::mutex mtx;
        std::condition_variable cv;
        std::queue<chan*> ready;
    }
     */
class engine {
	private int active;
	private Lock mtx;
	private Condition cv;
	private Queue<chan> ready;
}
/*
struct proc{
    std::mutex mtx;
    void(*recv)(chan*, proc*);
};
*/
abstract class proc{ //кажется это интерфейсы
	private Lock mtx;
	public abstract void recv (chan chan,proc proc);
}

/*
struct chan{
    proc*p;
    bool sending;
};
*/
class chan{
	private proc p;
	private boolean sending;
}

    /*
    inline void duration(engine*e, double t){}
    */

	public void duration (engine e, double t){}

	/*
    inline void send(engine*e, chan*c, proc*p)
    {
    if (c->sending) return;
    c->sending = true;	c->p = p;
    std::unique_lock<std::mutex> lck(e->mtx);
    e->ready.push(c);	e->cv.notify_one();
}
     */
	public void send(engine e,chan c, proc p)
	{
		if (c.sending)
		{
			return;
		}
		c.sending = true;
		c.p=p;
		//start of the critical section
		e.mtx.tryLock();
		e.ready.add(c); //Нужно ли делать unlock()?
		e.cv.notify();
		//end of the critical section
	}
/*
    inline bool access(chan*c, proc*p)
    {
        return c->p == p && !c->sending;
    }
*/

	public boolean access(chan c, proc p)
	{
		return c.p == p && !c.sending;
	}
	/*
        inline void tfunc(engine*e)
        {
            chan*c; proc*p;

            for (;;){
                {
                    std::unique_lock<std::mutex> lck(e->mtx);
                    while (e->ready.empty()){
                        e->active--;
                        if (!e->active){ e->cv.notify_one(); return; }
                        e->cv.wait(lck);
                        e->active++;
                    }
                    c = e->ready.front();
                    e->ready.pop();
                }
                p = c->p;
                {
                    std::unique_lock<std::mutex> lck(p->mtx);
                    c->sending = false;
                    p->recv(c, p);
                }
            }
        }
    */
	public void tfunc (engine e)
	{
		chan c;
		proc p;

		try {

			for (; ; ) {
				//start of the critical section
				e.mtx.tryLock();
				while (e.ready.isEmpty()) {
					e.active--;
					if (!(e.active == 0)) {
						e.cv.notify();
						return;
					}
					e.mtx.wait();
					e.active++; //Нужно ли делать unlock()?
					//end of the critical section
				}
				c = e.ready.poll(); //Retrieves and removes the head of this queue, or returns null if this queue is empty.

				p = c.p;
				//start of the critical section
				p.mtx.tryLock();
				c.sending = false;
				p.recv(c, p);
				//end of the critical section
			}
		}
		catch (InterruptedException interExc)
		{
			interExc.getMessage();
		}
	}
}
