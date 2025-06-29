FROM fluidity/baseimages:groovy

USER root

RUN apt-get -y update && \
      apt-get -y dist-upgrade && \
      apt-get -y install sudo && \
      rm -rf /var/cache/apt/archives && \
      rm -rf /var/lib/apt/lists

RUN adduser fluidity sudo
RUN echo '%sudo ALL=(ALL) NOPASSWD:ALL' >> /etc/sudoers

COPY . /home/fluidity
RUN chown -R fluidity /home/fluidity

USER fluidity

ENV FCFLAGS="-I/usr/include"

RUN ./configure --enable-2d-adaptivity
RUN make makefiles
RUN test -z "$(git status --porcelain */Makefile.dependencies)"
RUN make
RUN make fltools
RUN make manual
