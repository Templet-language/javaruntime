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
/*
using namespace TEMPLET_DBG;

double x;
double r1,r2;

engine e;
chan cc1,cc2;
proc pp,p1,p2;

void c1(chan*c, proc*)
{
	r1=sin(x)*sin(x);
	send(&e,c,&pp);
}

void c2(chan*c, proc*)
{
	r2=cos(x)*cos(x);
	send(&e,c,&pp);
}

void p(chan*, proc*p)
{
	if(access(&cc1,p) && access(&cc2,p)){
		cout<<r1+r2;
	}
}

int main(int argc, char*argv[])
{
	pp.recv=p;
	p1.recv=c1;
	p2.recv=c2;

	x=0.333;

	send(&e,&cc1,&p1);
	send(&e,&cc2,&p2);

	run(&e, 2);

	return 0;
}
*/
