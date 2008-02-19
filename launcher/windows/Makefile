# -*- Makefile -*-
#
# The contents of this file are subject to the terms of the Common Development
# and Distribution License (the License). You may not use this file except in
# compliance with the License..
#
# You can obtain a copy of the License at http://www.netbeans.org/cddl.html
# or http://www.netbeans.org/cddl.txt.
#
# When distributing Covered Code, include this CDDL Header Notice in each file
# and include the License file at http://www.netbeans.org/cddl.txt.
# If applicable, add the following below the CDDL Header, with the fields
# enclosed by brackets [] replaced by your own identifying information:
# "Portions Copyrighted [year] [name of copyright owner]"
#
# The Original Software is GraphMaker. The Initial Developer of the Original
# Software is Nathan L. Fiedler. Portions created by Nathan L. Fiedler
# are Copyright (C) 2005-2007. All Rights Reserved.
#
# Contributor(s): Nathan L. Fiedler.
#
# $Id: Makefile 2900 2007-03-06 09:17:53Z nfiedler $
#

CFLAGS = -s -mno-cygwin

all : clean graphmaker.exe graphmakerw.exe

graphmaker.exe : graphmaker.cpp graphmaker.res
	gcc $(CFLAGS) -mwindows -DWINMAIN graphmaker.cpp -o graphmaker.exe graphmaker.res

graphmakerw.exe : graphmaker.cpp
	gcc $(CFLAGS) graphmaker.cpp -o graphmakerw.exe

graphmaker.res : graphmaker.rc
	windres -o$@ -Ocoff graphmaker.rc

clean :
	rm -f *.res *.obj *.exe 2>NUL
