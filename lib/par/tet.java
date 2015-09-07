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

struct engine;
struct proc;
struct chan;

struct engine{
	volatile int active;
	std::mutex mtx;
	std::condition_variable cv;
	std::queue<chan*> ready;
};

struct proc{
	std::mutex mtx;
	void(*recv)(chan*, proc*);
};

struct chan{
	proc*p;
	bool sending;
};

inline void duration(engine*e, double t){}

inline void send(engine*e, chan*c, proc*p)
{
	if (c->sending) return;
	c->sending = true;	c->p = p;
	std::unique_lock<std::mutex> lck(e->mtx);
	e->ready.push(c);	e->cv.notify_one();
}

inline bool access(chan*c, proc*p)
{
	return c->p == p && !c->sending;
}

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

inline void run(engine*e, int n = 1)
{
	std::vector<std::thread> threads(n);
	e->active = n;
	for (int i = 0; i<n; i++) threads[i] = std::thread(tfunc, e);
	for (auto& th : threads) th.join();
}

inline void stat(engine*e, double&T1, double&Tp, int&Pmax, double&Smax, int P, double&Sp){}

*/