#ifndef HEADER_myblas
#define HEADER_myblas

/* ************************************************************************ */
/* BLAS function interface with local and external loadable versions        */
/* Author:  Kjell Eikland                                                   */
/* Version: Initial version spring 2004                                     */
/* Licence: LGPL                                                            */
/* ************************************************************************ */
/* Changes: 19 September 2004   Moved function pointer variable             */
/*                              declarations from myblas.h to myblas.c      */
/*                              to avoid linker problems with the Mac.      */
/*          20 April 2005       Modified all double types to LPSREAL to self-  */
/*                              adjust to global settings.  Note that BLAS  */
/*                              as of now does not have double double.      */
/* ************************************************************************ */

#define BLAS_BASE         1
#define UseMacroVector
#if defined LoadableBlasLib
#  if LoadableBlasLib == 0
#    undef LoadableBlasLib
#  endif
#else
#  define LoadableBlasLib
#endif


/* ************************************************************************ */
/* Include necessary libraries                                              */
/* ************************************************************************ */
#include "commonlib.h"
#ifdef LoadableBlasLib
  #ifdef WIN32
    #include <windows.h>
  #else
    #include <dlfcn.h>
  #endif
#endif


#ifdef __cplusplus
extern "C" {
#endif


/* ************************************************************************ */
/* BLAS functions                                                           */
/* ************************************************************************ */

#ifndef BLAS_CALLMODEL
#ifdef WIN32
# define BLAS_CALLMODEL _cdecl
#else
# define BLAS_CALLMODEL
#endif
#endif

typedef void   (BLAS_CALLMODEL BLAS_dscal_func) (int *n, LPSREAL *da, LPSREAL *dx, int *incx);
typedef void   (BLAS_CALLMODEL BLAS_dcopy_func) (int *n, LPSREAL *dx, int *incx,  LPSREAL *dy, int *incy);
typedef void   (BLAS_CALLMODEL BLAS_daxpy_func) (int *n, LPSREAL *da, LPSREAL *dx, int *incx,  LPSREAL *dy, int *incy);
typedef void   (BLAS_CALLMODEL BLAS_dswap_func) (int *n, LPSREAL *dx, int *incx,  LPSREAL *dy, int *incy);
typedef double (BLAS_CALLMODEL BLAS_ddot_func)  (int *n, LPSREAL *dx, int *incx,  LPSREAL *dy, int *incy);
typedef int    (BLAS_CALLMODEL BLAS_idamax_func)(int *n, LPSREAL *x,  int *is);
typedef void   (BLAS_CALLMODEL BLAS_dload_func) (int *n, LPSREAL *da, LPSREAL *dx, int *incx);
typedef double (BLAS_CALLMODEL BLAS_dnormi_func)(int *n, LPSREAL *x);

#ifndef __WINAPI
# ifdef WIN32
#  define __WINAPI WINAPI
# else
#  define __WINAPI
# endif
#endif

void init_BLAS(void);
MYBOOL is_nativeBLAS(void);
MYBOOL load_BLAS(char *libname);
MYBOOL unload_BLAS(void);

/* ************************************************************************ */
/* User-callable BLAS definitions (C base 1)                                */
/* ************************************************************************ */
void dscal ( int n, LPSREAL da,  LPSREAL *dx, int incx );
void dcopy ( int n, LPSREAL *dx, int incx, LPSREAL *dy, int incy );
void daxpy ( int n, LPSREAL da,  LPSREAL *dx, int incx,   LPSREAL *dy, int incy );
void dswap ( int n, LPSREAL *dx, int incx, LPSREAL *dy, int incy );
LPSREAL ddot  ( int n, LPSREAL *dx, int incx, LPSREAL *dy, int incy );
int  idamax( int n, LPSREAL *x,  int is );
void dload ( int n, LPSREAL da,  LPSREAL *dx, int incx );
LPSREAL dnormi( int n, LPSREAL *x );


/* ************************************************************************ */
/* Locally implemented BLAS functions (C base 0)                            */
/* ************************************************************************ */
void BLAS_CALLMODEL my_dscal ( int *n, LPSREAL *da, LPSREAL *dx,  int *incx );
void BLAS_CALLMODEL my_dcopy ( int *n, LPSREAL *dx, int *incx, LPSREAL *dy, int *incy );
void BLAS_CALLMODEL my_daxpy ( int *n, LPSREAL *da, LPSREAL *dx,  int *incx,  LPSREAL *dy, int *incy );
void BLAS_CALLMODEL my_dswap ( int *n, LPSREAL *dx, int *incx, LPSREAL *dy, int *incy );
LPSREAL BLAS_CALLMODEL my_ddot  ( int *n, LPSREAL *dx, int *incx,  LPSREAL *dy, int *incy );
int  BLAS_CALLMODEL my_idamax( int *n, LPSREAL *x,  int *is );
void BLAS_CALLMODEL my_dload ( int *n, LPSREAL *da, LPSREAL *dx, int *incx );
LPSREAL BLAS_CALLMODEL my_dnormi( int *n, LPSREAL *x );


/* ************************************************************************ */
/* Subvector and submatrix access routines (Fortran compatibility)          */
/* ************************************************************************ */
#ifdef UseMacroVector
  #define subvec(item) (item - 1)
#else
  int subvec( int item );
#endif

int submat( int nrowb, int row, int col );
int posmat( int nrowb, int row, int col );


/* ************************************************************************ */
/* Randomization functions                                                  */
/* ************************************************************************ */
void randomseed(int *seeds);
void randomdens( int n, LPSREAL *x, LPSREAL r1, LPSREAL r2, LPSREAL densty, int *seeds);
void ddrand( int n, LPSREAL *x, int incx, int *seeds );


#ifdef __cplusplus
}
#endif

#endif
