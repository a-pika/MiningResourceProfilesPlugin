// -*- mode: C++; c-indent-level: 4; c-basic-offset: 4; tab-width: 8 -*-
//
// Col_proto.h: Rcpp/Armadillo glue
//
// Copyright (C)  2010 Dirk Eddelbuettel, Romain Francois and Douglas Bates
//
// This file is part of RcppArmadillo.
//
// RcppArmadillo is free software: you can redistribute it and/or modify it
// under the terms of the GNU General Public License as published by
// the Free Software Foundation, either version 2 of the License, or
// (at your option) any later version.
//
// RcppArmadillo is distributed in the hope that it will be useful, but
// WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
// GNU General Public License for more details.
//
// You should have received a copy of the GNU General Public License
// along with RcppArmadillo.  If not, see <http://www.gnu.org/licenses/>.


#ifndef RCPPARMADILLO_COL_PROTO_H
#define RCPPARMADILLO_COL_PROTO_H

template <int RTYPE, bool NA, typename VECTOR>
inline Col( const Rcpp::VectorBase<RTYPE,NA,VECTOR>& X ) ;

template <int RTYPE, bool NA, typename VECTOR>
inline Col( const Rcpp::MatrixBase<RTYPE,NA,VECTOR>& X ) ;

#endif
