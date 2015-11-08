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
package sim;

/*
struct event{
	double time;
	enum{ CHAN, PROC0, PROC1 } type;
	chan*c;
	proc*p;
};*/

	class event {
			double time;
			enum type { //TODO проверить оформление
				CHAN, PROC0,PROC1
			}
			chan c;
			proc p;
		}

/*
class cmp{ public: bool operator()(const event&t1, const event&t2){ return t1.time > t2.time; } };*/
	class cmp{
		public boolean operator(event t1, event t2){
			//final event t1, t2; //параметры или локальные переменные?
			return  t1.time > t2.time;
		}
	}
/*	
struct engine{
	std::priority_queue<event, std::vector<event>, cmp> calendar;
	double Tp;
	double T1;
	int Pmax;
};*/


/*
struct proc{
	bool lck;
	std::queue<chan*> ready;
	void(*recv)(chan*, proc*);
};*/
/*
struct chan{
	proc*p;
	bool sending;
};*/
/*
inline void duration(engine*e, double t)
{
	e->T1 += t;
	e->Tp += t;
}*/
/*
inline void send(engine*e, chan*c, proc*p)
{
	if (c->sending) return;
	c->sending = true;	c->p = p;

	event ev;
	ev.time = e->Tp; ev.type = event::CHAN; ev.c = c;
	e->calendar.push(ev);
}*/
/*
inline bool access(chan*c, proc*p)
{
	return c->p == p && !c->sending;
}*/
/*
inline void run(engine*e, int n = 1)
{
	proc*p = 0; chan*c = 0;
	double Tcur = 0.0, Tprev = 0.0;
	int Pcur = 0, Pmax = 0;

	while (!e->calendar.empty()){
		event ev;
		ev = e->calendar.top();	e->calendar.pop();

		Tcur = ev.time;
		if (Tcur - Tprev > 0 && Pcur > Pmax) Pmax = Pcur;
		Tprev = Tcur;

		switch (ev.type){
		case event::CHAN:
		{
			p = ev.c->p;
			if (!p->lck && p->ready.empty()){
				ev.p = p; ev.type = event::PROC0;
				e->calendar.push(ev);
			}
			p->ready.push(ev.c);
		}
		break;

		case event::PROC0:
		{
			p = ev.p;
			c = p->ready.front(); p->ready.pop();
			c->sending = false;

			p->lck = true; Pcur++;

			e->Tp = Tcur; p->recv(c, p); Tcur = e->Tp;

			ev.time = Tcur; ev.type = event::PROC1;
			e->calendar.push(ev);
		}
		break;

		case event::PROC1:
		{
			p = ev.p;
			if (!p->ready.empty()){
				ev.type = event::PROC0;
				e->calendar.push(ev);
			}
			p->lck = false; Pcur--;
		}
		}
	}
	e->Tp = Tcur; e->Pmax = Pmax;
}*/
/*
inline void stat(engine*e, double&T1, double&Tp, int&Pmax, double&Smax, int P, double&Sp)
{
	T1 = e->T1; Tp = e->Tp; Pmax = e->Pmax;
	Smax = T1 / Tp;
	double alfa = (1 - Smax / Pmax) / (Smax - Smax / Pmax);
	Sp = (P > Pmax) ? Smax : 1 / (alfa + (1 - alfa) / P);
}

*/
}
