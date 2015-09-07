/*$TET$templet$!cpp-copyright!*/
/*--------------------------------------------------------------------------*/
/*  Copyright 2010-2015 Sergey Vostokin                                     */
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
/*$TET$*/

/*$TET$templet$!templet!*/
/*~Link=
	+Begin ? argSin2 -> Calc | argCos2 -> Calc;
	 Calc  ! result  -> End;
	 End.
*Parent=
	 p1 : Link ! result -> join;
	 p2 : Link ! result -> join;
	+fork(p1!argCos2,p2!argSin2);
	 join(p1?result,p2?result).
*Child=
	p : Link ? argCos2 -> calcCos2 | argSin2 -> calcSin2;
	calcCos2(p?argCos2,p!result);
	calcSin2(p?argSin2,p!result).*/
/*$TET$*/

#include "templet.h"

/*$TET$templet$!cpp-prologue!*/
/*$TET$*/
//////////////////////class Link////////////////////
Link::Link(TEMPLET_DBG::Assemble*a):TEMPLET_DBG::Channel(a)
{
	_state=_st_Begin;
	_sent=_no_snt;
/*$TET$Link$!constructor!*/
/*$TET$*/
}

Link::~Link()
{
/*$TET$Link$!destructor!*/
/*$TET$*/
}

void Link::_send_argSin2()
{
	assert( _state==_st_Begin || true );
	_sent=_snt_argSin2;
	if(_state==_st_Begin){_state=_st_Calc;_active=RTL_SRV;}
	_send();
}

void Link::_send_argCos2()
{
	assert( _state==_st_Begin || true );
	_sent=_snt_argCos2;
	if(_state==_st_Begin){_state=_st_Calc;_active=RTL_SRV;}
	_send();
}

void Link::_send_result()
{
	assert( _state==_st_Calc || true );
	_sent=_snt_result;
	if(_state==_st_Calc){_state=_st_End;_active=RTL_CLI;}
	_send();
}

//////////////////////class Parent////////////////////
Parent::Parent(TEMPLET_DBG::Assemble*a):TEMPLET_DBG::Process(a)
{
	_p1=0;
	_p2=0;
	_entry=_createActivator();_entry->_send(_method_fork);
/*$TET$Parent$!constructor!*/
/*$TET$*/
}

Parent::~Parent()
{
	delete _entry;
/*$TET$Parent$!destructor!*/
/*$TET$*/
}

/*$TET$Parent$!usercode!*/
/*$TET$*/

bool Parent::fork(/*out*/Link::argCos2*p1,/*out*/Link::argSin2*p2)
{
/*$TET$Parent$fork*/
	p1->x=p2->x=0.333;
	return true;
/*$TET$*/
}

bool Parent::join(/*in*/Link::result*p1,/*in*/Link::result*p2)
{
/*$TET$Parent$join*/
	cout<<"sin2x+cos2x="<<p1->y+p2->y;
	return true;
/*$TET$*/
}

void Parent::_run(int _selector,TEMPLET_DBG::Channel*_channel)
{
	bool res;
/*$TET$Parent$!run!*/
/*$TET$*/
	for(;;){
		switch(_selector){
			case _port_p1://
				if(_p1->_c_in_result()){_selector=_method_join;break;};
				assert(0);
				return;
			case _port_p2://
				if(_p2->_c_in_result()){_selector=_method_join;break;};
				assert(0);
				return;
			case _method_fork:
				res=(_p1->_c_out_argCos2()&&_p2->_c_out_argSin2());
				if(res)res=fork(_p1->_get_argCos2(),_p2->_get_argSin2());
				if(res)_p1->_send_argCos2();
				if(res)_p2->_send_argSin2();
				return;
			case _method_join:
				res=(_p1->_c_in_result()&&_p2->_c_in_result());
				if(res)res=join(_p1->_get_result(),_p2->_get_result());
				return;
			default: assert(0); return;
		}
	}
}

//////////////////////class Child////////////////////
Child::Child(TEMPLET_DBG::Assemble*a):TEMPLET_DBG::Process(a)
{
	_p=0;
/*$TET$Child$!constructor!*/
/*$TET$*/
}

Child::~Child()
{
/*$TET$Child$!destructor!*/
/*$TET$*/
}

/*$TET$Child$!usercode!*/
/*$TET$*/

bool Child::calcCos2(/*in*/Link::argCos2*p1,/*out*/Link::result*p2)
{
/*$TET$Child$calcCos2*/
	p2->y=cos(p1->x)*cos(p1->x);
	return true;
/*$TET$*/
}

bool Child::calcSin2(/*in*/Link::argSin2*p1,/*out*/Link::result*p2)
{
/*$TET$Child$calcSin2*/
	p2->y=sin(p1->x)*sin(p1->x);
	return true;
/*$TET$*/
}

void Child::_run(int _selector,TEMPLET_DBG::Channel*_channel)
{
	bool res;
/*$TET$Child$!run!*/
/*$TET$*/
	for(;;){
		switch(_selector){
			case _port_p://
				if(_p->_s_in_argCos2()){_selector=_method_calcCos2;break;};
				if(_p->_s_in_argSin2()){_selector=_method_calcSin2;break;};
				assert(0);
				return;
			case _method_calcCos2:
				res=(_p->_s_in_argCos2()&&_p->_s_out_result());
				if(res)res=calcCos2(_p->_get_argCos2(),_p->_get_result());
				if(res)_p->_send_result();
				return;
			case _method_calcSin2:
				res=(_p->_s_in_argSin2()&&_p->_s_out_result());
				if(res)res=calcSin2(_p->_get_argSin2(),_p->_get_result());
				if(res)_p->_send_result();
				return;
			default: assert(0); return;
		}
	}
}

//////////////////////class templet////////////////////
templet::templet(int NT): TEMPLET_DBG::Assemble(NT)
{
/*$TET$templet$!constructor!*/
/*$TET$*/
}

templet::~templet()
{
/*$TET$templet$!destructor!*/
/*$TET$*/
}

/*$TET$templet$!cpp-epilogue!*/
int main(int argc, char*argv[])
{
	templet tet(2);
	
	Parent* p=tet.new_Parent();
	Child*  c1=tet.new_Child();
	Child*  c2=tet.new_Child();

	c1->p_p(p->p_p1());
	c2->p_p(p->p_p2());

	tet.run();
	
	return 0;
}
/*$TET$*/

