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
	std::vector<chan*> ready;
};

struct proc{
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
	c->sending = true;
	c->p = p;
	e->ready.push_back(c);
}

inline bool access(chan*c, proc*p)
{
	return c->p == p && !c->sending;
}

inline void run(engine*e, int n = 1)
{
	size_t rsize;
	while (rsize = e->ready.size()){
		int n = rand() % rsize;	auto it = e->ready.begin() + n;
		chan*c = *it;	e->ready.erase(it); c->sending = false;
		c->p->recv(c, c->p);
	}
}

inline void stat(engine*e, double&T1, double&Tp, int&Pmax, double&Smax, int P, double&Sp){}
*/