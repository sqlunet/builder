#!/bin/bash

thisdir="`dirname $(readlink -m $0)`/"
thisdir="$(readlink -m ${thisdir})"

source define_colors.sh
source list.sh

imgdir="$(readlink -m ${thisdir}/svg)"
#echo "imgdir=${imgdir}"
pdfdir="$(readlink -m ${thisdir}/pdf)"
#echo "pdfdir=${pdfdir}"
mkdir -p ${pdfdir}
#rm -f ${pdfdir}/*pdf

# S V G   T O   P D F 

echo -e "${Y}svg to pdf${Z}"
pushd ${imgdir} > /dev/null
for f in ${files}; do 
	f="${f}.svg"
	if [ ! -e "${f}" ]; then
		echo -e "${R}* ${f} does not exist${Z}"
		continue
	fi	
	b=${f%.svg};
	
	echo -e "${B}${b}.svg${Z} -> ${C}${b}.pdf${Z}" 
	#rm ${pdfdir}/${b}.pdf 2> /dev/null
	inkscape ${b}.svg -z --export-pdf=${pdfdir}/${b}.pdf > /dev/null 2> /dev/null
done
popd > /dev/null

#echo waiting for PDF print completion...
#sleep 25

# R O T A T E

echo -e "${Y}rotate${Z}"
pushd ${pdfdir} > /dev/null
for f in uml*.pdf fks-*.pdf; do 
	b=`basename "${f}"`
	b=${b%.*}
	if [[ ${b} == *"rotated"* ]]; then
  		continue
	fi
	pdftk ${f} rotate 1-endwest output ${b}-rotated.pdf
	echo -e "${B}${f}${Z} -> ${C}${b}-rotated.pdf${Z}"
done
popd > /dev/null


# A S S E M B L E

echo -e "${Y}assemble${Z}"
pushd ${pdfdir} > /dev/null
for k in ${domains}; do
	umlpdf="${umlpdf}`ls uml-${k}*-rotated.pdf`
"
	tablepdf="${tablepdf}`ls tables-${k}*.pdf`
"
	fkpdf="${fkpdf}`ls fks-*${k}*-rotated.pdf`
"
	schemapdf="${schemapdf}`ls schemas-${k}*.pdf`
"
	mwbpdf="${mwbpdf}`ls mwb-${k}*.pdf`
"
done

p1=sqlunet-front.pdf
puml=sqlunet-sep-uml.pdf
pschemas=sqlunet-sep-schemas.pdf
ptables=sqlunet-sep-tables.pdf
pmwb=sqlunet-sep-mwb.pdf

allpdfs="
${puml} 
${umlpdf} 
${pschemas} 
${schemapdf} 
${ptables} 
${tablepdf} 
${fkpdf} 
${pmwb} 
${mwbpdf}"
echo -e "pdfs=${M}${allpdfs}${Z}"
#echo ${allpdfs} | tr '\n' ' '

param="--pdftitle 'SqlUNet2' --pdfauthor 'Bernard Bou' --paper A4 --twoside --no-landscape --rotateoversize 'false'"
outfile=$(readlink -m ../sqlunet2.pdf)
pdfjam ${params} --outfile "${outfile}" ${p1} ${allpdfs}
echo -e "${G}${outfile}${Z}"
popd > /dev/null

