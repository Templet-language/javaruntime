/*$TET$templet$!h-copyright!*/
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

#ifndef _TEMPLET_MODULE_templet
#define _TEMPLET_MODULE_templet

#include <string.h>
#include <assert.h>

#include "dbg/tet.h"

/*$TET$templet$!h-prologue!*/
#include <iostream>
using namespace std;
/*$TET$*/

class Link:public TEMPLET_DBG::Channel{
public:
	Link(TEMPLET_DBG::Assemble*a);
	~Link();
public:
	struct cli{Link*c;}_cli;
	struct srv{Link*c;}_srv;
public:
	struct argSin2{//<Begin> 
/*$TET$Link$argSin2*/
	double x;
/*$TET$*/
	};
	struct argCos2{//<Begin> 
/*$TET$Link$argCos2*/
	double x;
/*$TET$*/
	};
	struct result{//<Calc> 
/*$TET$Link$result*/
	double y;
/*$TET$*/
	};
public:
	void _send_argSin2();
	void _send_argCos2();
	void _send_result();
public:
	argSin2* _get_argSin2(){return &_mes_argSin2;}
	argCos2* _get_argCos2(){return &_mes_argCos2;}
	result* _get_result(){return &_mes_result;}
private:
	Link::argSin2  _mes_argSin2;
	Link::argCos2  _mes_argCos2;
	Link::result  _mes_result;
private:
	enum _state_Iface{
		_st_Begin,
		_st_Calc,
		_st_End
	};
	enum _sent_Iface{_no_snt,
		_snt_argSin2,
		_snt_argCos2,
		_snt_result
	};
private:
	enum _state_Iface _state;
	enum _sent_Iface _sent;
public:
	// access tests for message 'argSin2'
	bool _s_in_argSin2(){return _active==SRV && _sent==_snt_argSin2;}
	bool _c_in_argSin2(){return _active==CLI && _sent==_snt_argSin2;}
	bool _s_out_argSin2(){return _active==SRV && (false);}
	bool _c_out_argSin2(){return _active==CLI && (_state==_st_Begin||false);}
	// access tests for message 'argCos2'
	bool _s_in_argCos2(){return _active==SRV && _sent==_snt_argCos2;}
	bool _c_in_argCos2(){return _active==CLI && _sent==_snt_argCos2;}
	bool _s_out_argCos2(){return _active==SRV && (false);}
	bool _c_out_argCos2(){return _active==CLI && (_state==_st_Begin||false);}
	// access tests for message 'result'
	bool _s_in_result(){return _active==SRV && _sent==_snt_result;}
	bool _c_in_result(){return _active==CLI && _sent==_snt_result;}
	bool _s_out_result(){return _active==SRV && (_state==_st_Calc||false);}
	bool _c_out_result(){return _active==CLI && (false);}
};

class Parent:public TEMPLET_DBG::Process{
public:
	Parent(TEMPLET_DBG::Assemble*a);
	~Parent();
private:
	//methods
	bool fork(/*out*/Link::argCos2*p1,/*out*/Link::argSin2*p2);
	bool join(/*in*/Link::result*p1,/*in*/Link::result*p2);

/*$TET$Parent$!userdata!*/
/*$TET$*/

public:
	enum{
		_port_p1,
		_port_p2,
		_method_fork,
		_method_join
	};
public:
	void p_p1(Link::cli*p){_p1=(Link*)p->c;_p1->_cliPort=this;_p1->_cli_selector=_port_p1;}
	Link* p_p1(){ _p1 = !_p1 ? _assemble->_regChan(_p1), new Link(_assemble) : _p1; _p1->_cliPort = this; _p1->_cli_selector = _port_p1; return _p1; }
	void p_p2(Link::cli*p){_p2=(Link*)p->c;_p2->_cliPort=this;_p2->_cli_selector=_port_p2;}
	Link* p_p2(){ _p2 = !_p2 ? _assemble->_regChan(_p2), new Link(_assemble) : _p2; _p2->_cliPort = this; _p2->_cli_selector = _port_p2; return _p2; }
protected:
	virtual void _run(int _selector,TEMPLET_DBG::Channel*_channel);
private:
	// ports
	Link* _p1;
	Link* _p2;
	// initial activator
	TEMPLET_DBG::Activator* _entry;
};

class Child:public TEMPLET_DBG::Process{
public:
	Child(TEMPLET_DBG::Assemble*a);
	~Child();
private:
	//methods
	bool calcCos2(/*in*/Link::argCos2*p1,/*out*/Link::result*p2);
	bool calcSin2(/*in*/Link::argSin2*p1,/*out*/Link::result*p2);

/*$TET$Child$!userdata!*/
/*$TET$*/

public:
	enum{
		_port_p,
		_method_calcCos2,
		_method_calcSin2
	};
public:
	void p_p(Link::srv*p){_p=(Link*)p->c;_p->_srvPort=this;_p->_srv_selector=_port_p;}
	void p_p(Link*p){ _p = p; _p->_srvPort = this; _p->_srv_selector = _port_p; }
protected:
	virtual void _run(int _selector,TEMPLET_DBG::Channel*_channel);
private:
	// ports
	Link* _p;
	// initial activator
};

class templet:public TEMPLET_DBG::Assemble{
public:
	templet(int NT);
	~templet();

/*$TET$templet$!userdata!*/
/*$TET$*/
public:
	Parent*new_Parent(){Parent*p=new Parent(this);_regProc(p);return p;}
	Child*new_Child(){Child*p=new Child(this);_regProc(p);return p;}
	void new_Link(Link::cli*&c,Link::srv*&s){Link*ch=new Link(this);_regChan(ch);
		ch->_cli.c=ch->_srv.c=ch;c=&ch->_cli;s=&ch->_srv;}
};

/*$TET$templet$!h-epilogue!*/
/*$TET$*/
#endif
